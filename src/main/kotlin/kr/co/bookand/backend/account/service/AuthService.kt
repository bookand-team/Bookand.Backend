package kr.co.bookand.backend.account.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import kr.co.bookand.backend.account.dto.*
import kr.co.bookand.backend.account.model.*
import kr.co.bookand.backend.account.repository.AccountRepository
import kr.co.bookand.backend.account.repository.SuspendedAccountRepository
import kr.co.bookand.backend.bookmark.model.Bookmark
import kr.co.bookand.backend.bookmark.model.BookmarkType
import kr.co.bookand.backend.bookmark.repository.BookmarkRepository
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.RestTemplateService
import kr.co.bookand.backend.common.model.MessageResponse
import kr.co.bookand.backend.common.exception.BookandException
import kr.co.bookand.backend.config.jwt.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.MultiValueMap
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.time.LocalDateTime
import java.util.*


@Service
@Transactional(readOnly = true)
class AuthService(
    private val jwtProvider: JwtProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val apiService: RestTemplateService<MultiValueMap<String, String>>,
    private val passwordEncoder: PasswordEncoder,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val accountRepository: AccountRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val suspendedAccountRepository: SuspendedAccountRepository
) {
    val responseType: ParameterizedTypeReference<Map<String, Any>> =
        object : ParameterizedTypeReference<Map<String, Any>>() {}

    @Value("\${bookand.suffix}")
    private lateinit var suffix: String

    @Transactional
    fun socialAccess(authRequest: AuthRequest): LoginResponse {
        val socialIdWithAccessToken = getSocialIdWithAccessToken(authRequest)
        authRequest.insertId(socialIdWithAccessToken.userId)
        val email = authRequest.extraEmail()
        val providerEmail = socialIdWithAccessToken.email
        val existAccount = accountRepository.findByEmail(email)
        val socialType = authRequest.getSocialType()

        if (existAccount != null) {
            // 로그인
            val tokenResponse =
                login(AccountInfoResponse(account = existAccount).toLoginRequest(suffix))
            return LoginResponse(
                tokenResponse = tokenResponse,
                httpStatus = HttpStatus.OK
            )
        } else {
            val middleAccount = MiddleAccount(
                email = email,
                providerEmail = providerEmail,
                socialType = socialType,
            )
            // 회원가입
            val signTokenResponse = jwtProvider.createSignTokenDto(middleAccount)
            return LoginResponse(
                tokenResponse = signTokenResponse,
                httpStatus = HttpStatus.NOT_FOUND
            )
        }
    }

    @Transactional
    fun socialSignUp(signTokenRequest: SignTokenRequest): TokenResponse {
        val signKey = jwtProvider.getSignKey(signTokenRequest)
        checkSignUp(signKey)
        val nickname = nicknameRandom()
        duplicateEmailAndNickName(signKey.email, nickname)
        val account = Account(
            email = signKey.email,
            nickname = nickname,
            password = passwordEncoder.encode(signKey.email + suffix),
            provider = signKey.socialType,
            providerEmail = signKey.providerEmail,
            role = Role.USER,
            accountStatus = AccountStatus.NORMAL,
            signUpDate = LocalDateTime.now(),
        )
        val saveAccount = accountRepository.save(account)
        saveAccount.updateLastLoginDate()

        val initBookmark = createInitBookmark(saveAccount)
        saveAccount.updateBookmarks(initBookmark)

        val tokenResponse = login(AccountInfoResponse(account = saveAccount).toLoginRequest(suffix))

        return TokenResponse(
            accessToken = tokenResponse.accessToken,
            refreshToken = tokenResponse.refreshToken
        )
    }

    fun login(loginRequest: LoginRequest): TokenResponse {
        val email = loginRequest.email
        val account = getAccountByEmail(email)
        checkSuspended(account)
        account.updateLastLoginDate()
        return getTokenDto(loginRequest)
    }

    @Transactional
    fun checkSuspended(account: Account) {
        val suspendedAccount = getSuspendedAccount(account) ?: return
        val isAccountExpired = LocalDateTime.now().isAfter(suspendedAccount.endedSuspendedAt)
        when (account.accountStatus) {
            AccountStatus.SUSPENDED -> {
                if (isAccountExpired) {
                    account.updateAccountStatus(AccountStatus.NORMAL)
                } else {
                    throw BookandException(ErrorCode.SUSPENDED_ACCOUNT)
                }
            }

            AccountStatus.DELETED -> throw BookandException(ErrorCode.DELETED_ACCOUNT)
            else -> return
        }
    }

    private fun checkRole(roleManager: String, authority: String) {
        if (roleManager != authority) {
            throw BookandException(ErrorCode.ROLE_ACCESS_ERROR)
        }
    }

    fun basicLogin(loginRequest: LoginRequest, requiredRole: Role): TokenResponse {
        val authenticationToken = loginRequest.toAuthenticationToken()
        val authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken)
        for (grantedAuthority in authentication.authorities) {
            val authority = grantedAuthority.authority
            checkRole(requiredRole.name, authority)
        }
        return getTokenDto(loginRequest)
    }

    fun adminLogin(loginRequestDto: LoginRequest): TokenResponse {
        return basicLogin(loginRequestDto, Role.ADMIN)
    }

    fun managerLogin(loginRequestDto: LoginRequest): TokenResponse {
        return basicLogin(loginRequestDto, Role.MANAGER)
    }


    @Transactional
    fun createManager(account: Account, managerInfoRequest: ManagerInfoRequest): MessageResponse {
        val admin = account
        admin.role.checkAdmin()
        duplicateEmailAndNickName(managerInfoRequest.email, managerInfoRequest.nickname)
        val account = Account(
            email = managerInfoRequest.email,
            password = passwordEncoder.encode(managerInfoRequest.password),
            nickname = managerInfoRequest.nickname,
            role = Role.MANAGER,
            provider = SocialType.GOOGLE.name,
            providerEmail = "providerEmail",
            accountStatus = AccountStatus.NORMAL
        )
        val saveAccount = accountRepository.save(account)
        saveAccount.updateBookmarks(createInitBookmark(account))
        return MessageResponse(result = "생성 완료", 200)
    }


    @Transactional
    fun logout(): MessageResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val loginAccount = authentication.name
        if (accountRepository.existsByEmail(loginAccount) || refreshTokenRepository.existsByKey(authentication.name)) {
            val refreshToken = refreshTokenRepository.findByKey(authentication.name)
                ?: throw BookandException(ErrorCode.NOT_FOUND_REFRESH_TOKEN)
            refreshTokenRepository.delete(refreshToken)
        } else {
            throw BookandException(ErrorCode.NOT_FOUND_MEMBER)
        }
        return MessageResponse(result = "로그아웃 성공", 200)
    }

    private fun createInitBookmark(saveAccount: Account): MutableList<Bookmark> {
        return getBookmarks(saveAccount, INIT_BOOKMARK_FOLDER_NAME, bookmarkRepository)
    }

    @Transactional
    fun getBookmarks(
        saveAccount: Account,
        initBookmarkFolderName: String,
        bookmarkRepository: BookmarkRepository
    ): MutableList<Bookmark> {
        val initBookmarkArticle = Bookmark(
            account = saveAccount,
            folderName = initBookmarkFolderName,
            bookmarkType = BookmarkType.ARTICLE
        )
        val initBookmarkBookStore = Bookmark(
            account = saveAccount,
            folderName = initBookmarkFolderName,
            bookmarkType = BookmarkType.BOOKSTORE
        )

        bookmarkRepository.save(initBookmarkArticle)
        bookmarkRepository.save(initBookmarkBookStore)
        return mutableListOf(initBookmarkArticle, initBookmarkBookStore)
    }


    private fun duplicateEmailAndNickName(email: String, nickname: String) {
        if (accountRepository.existsByEmail(email)) {
            throw BookandException(ErrorCode.EMAIL_DUPLICATION)
        }
        if (accountRepository.existsByNickname(nickname)) {
            throw BookandException(ErrorCode.NICKNAME_DUPLICATION)
        }
    }


    fun checkSignUp(signKey: SigningAccount) {
        if (signKey.email == null || signKey.socialType == null || signKey.providerEmail == null) {
            throw BookandException(ErrorCode.INVALID_SIGN_TOKEN)
        }
    }

    @Transactional
    fun reissue(tokenRequestDto: TokenRequest): TokenResponse {
        if (!jwtProvider.validateToken(tokenRequestDto.refreshToken)) {
            throw BookandException(ErrorCode.JWT_ERROR)
        }
        val authentication = jwtProvider.getAuthentication(tokenRequestDto.refreshToken)
        if (authentication != null) {
            println("authentication.name = ${authentication.name}")
        }
        val refreshToken = authentication?.let { refreshTokenRepository.findByKey(it.name) }
            ?: throw BookandException(ErrorCode.NOT_FOUND_REFRESH_TOKEN)

        checkSuspended(refreshToken.account)

        // 로그인 접근 시간 업데이트
        refreshToken.account.updateLastLoginDate()

        reissueRefreshExceptionCheck(refreshToken.value, tokenRequestDto)
        val tokenDto = jwtProvider.generateToken(authentication)

        // refresh token 업데이트
        refreshToken.updateValue(tokenDto.refreshToken)

        return TokenResponse(
            accessToken = tokenDto.accessToken,
            refreshToken = tokenDto.refreshToken
        )
    }

    private fun reissueRefreshExceptionCheck(refreshToken: String?, tokenRequestDto: TokenRequest) {
        if (refreshToken == null) {
            throw BookandException(ErrorCode.NOT_FOUND_REFRESH_TOKEN)
        }
        if (refreshToken != tokenRequestDto.refreshToken) {
            throw BookandException(ErrorCode.NOT_MATCH_REFRESH_TOKEN)
        }
    }


    fun nicknameRandom(): String {
        val url = "https://nickname.hwanmoo.kr/?format=json&count=1&max_length=10"
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        val request = HttpEntity<MultiValueMap<String, String>>(headers)
        val response: ResponseEntity<Map<String, Any>> =
            apiService.httpEntityPost(url, HttpMethod.GET, request, responseType)
        val stringObjectMap = response.body ?: throw RuntimeException("Failed to retrieve nickname")
        return stringObjectMap["words"].toString().replace("[", "").replace("]", "")
    }


    fun getTokenDto(loginRequest: LoginRequest): TokenResponse {
        val authentication = authenticationManagerBuilder.getObject().authenticate(loginRequest.toAuthenticationToken())
        val tokenDto = jwtProvider.generateToken(authentication)
        val account = getAccountByEmail(loginRequest.email)
        val refreshToken = RefreshToken(
            key = loginRequest.email,
            value = tokenDto.refreshToken,
            account = account
        )
        refreshTokenRepository.save(refreshToken)
        return tokenDto
    }


    fun getSocialIdWithAccessToken(data: AuthRequest): ProviderIdAndEmail {
        return when (data.socialType) {
            SocialType.GOOGLE.name -> getGoogleIdAndEmail(data)
            SocialType.APPLE.name -> getAppleId(data.accessToken)
            else -> throw BookandException(ErrorCode.NOT_FOUND_SOCIAL_TYPE)
        }
    }

    private fun getGoogleIdAndEmail(data: AuthRequest): ProviderIdAndEmail {
        val url = data.getSocialType().userInfoUrl
        val method = data.getSocialType().method
        val headers = setHeaders(data.accessToken)
        val request = HttpEntity<MultiValueMap<String, String>>(headers)
        val response: ResponseEntity<Map<String, Any>> = apiService.httpEntityPost(url, method, request, responseType)
        val userId = response.body?.get("sub") as String?
        val providerEmail = response.body?.get("email") as String?
        return ProviderIdAndEmail(userId, providerEmail)
    }

    private fun getAppleAuthPublicKey(): AppleDto {
        val url = SocialType.APPLE.userInfoUrl
        val method = SocialType.APPLE.method
        val headers = HttpHeaders()
        val request = HttpEntity<MultiValueMap<String, String>>(headers)
        val response: ResponseEntity<AppleDto> =
            apiService.getAppleKeys(url, method, request, AppleDto::class.java)
        return response.body
    }

    private fun getAppleId(identityToken: String): ProviderIdAndEmail {
        val appleKeyStorage = getAppleAuthPublicKey()
        try {
            val headerToken = identityToken.substring(0, identityToken.indexOf("."))
            val header: Map<String, String> = ObjectMapper().readValue(
                String(Base64.getDecoder().decode(headerToken), StandardCharsets.UTF_8),
                Map::class.java
            ) as Map<String, String>

            val key: AppleDto.AppleKey =
                appleKeyStorage.getMatchedKeyBy(header["kid"], header["alg"]).orElseThrow()

            val nBytes = Base64.getUrlDecoder().decode(key.n)
            val eBytes = Base64.getUrlDecoder().decode(key.e)

            val n = BigInteger(1, nBytes)
            val e = BigInteger(1, eBytes)

            val publicKeySpec = RSAPublicKeySpec(n, e)
            val keyFactory = KeyFactory.getInstance(key.kty)
            val publicKey: PublicKey = keyFactory.generatePublic(publicKeySpec)

            val claims: Claims =
                Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(identityToken).body
            val subject = claims.subject
            val email = claims["email"] as String?
            return ProviderIdAndEmail(subject, email)
        } catch (e: Exception) {
            throw BookandException(ErrorCode.APPLE_LOGIN_ERROR)
        }
    }

    fun setHeaders(accessToken: String): HttpHeaders {
        val headers = HttpHeaders()
        headers["Authorization"] = "Bearer $accessToken"
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        return headers
    }

    fun getAccountByEmail(email: String): Account {
        return accountRepository.findByEmail(email)
            ?: throw BookandException(ErrorCode.NOT_FOUND_MEMBER)
    }

    fun getSuspendedAccount(account: Account): SuspendedAccount? {
        return suspendedAccountRepository.findByAccount(account)
    }

    private companion object {
        const val INIT_BOOKMARK_FOLDER_NAME = "모아보기"
    }
}