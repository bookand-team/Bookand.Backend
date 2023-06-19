package kr.co.bookand.backend.account.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import kr.co.bookand.backend.account.domain.*
import kr.co.bookand.backend.account.domain.dto.*
import kr.co.bookand.backend.account.repository.KotlinAccountRepository
import kr.co.bookand.backend.account.repository.KotlinSuspendedAccountRepository
import kr.co.bookand.backend.bookmark.domain.BookmarkType
import kr.co.bookand.backend.bookmark.domain.KotlinBookmark
import kr.co.bookand.backend.bookmark.domain.KotlinBookmarkType
import kr.co.bookand.backend.bookmark.repository.KotlinBookmarkRepository
import kr.co.bookand.backend.common.KotlinErrorCode
import kr.co.bookand.backend.common.KotlinRestTemplateService
import kr.co.bookand.backend.common.domain.KotlinMessageResponse
import kr.co.bookand.backend.config.jwt.*
import kr.co.bookand.backend.config.security.KotlinSecurityUtils
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
class KotlinAuthService(
    private val jwtProvider: JwtProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val apiService: KotlinRestTemplateService<MultiValueMap<String, String>>,
    private val passwordEncoder: PasswordEncoder,
    private val kotlinRefreshTokenRepository: KotlinRefreshTokenRepository,
    private val kotlinAccountRepository: KotlinAccountRepository,
    private val kotlinBookmarkRepository: KotlinBookmarkRepository,
    private val kotlinSuspendedAccountRepository: KotlinSuspendedAccountRepository
) {
    val responseType: ParameterizedTypeReference<Map<String, Any>> =
        object : ParameterizedTypeReference<Map<String, Any>>() {}

    @Value("\${bookand.suffix}")
    private lateinit var suffix: String

    @Transactional
    fun socialAccess(kotlinAuthRequest: KotlinAuthRequest): KotlinLoginResponse {
        val socialIdWithAccessToken = getSocialIdWithAccessToken(kotlinAuthRequest)
        kotlinAuthRequest.insertId(socialIdWithAccessToken.userId)
        val email = kotlinAuthRequest.extraEmail()
        val providerEmail = socialIdWithAccessToken.email
        val existAccount = kotlinAccountRepository.findByEmail(email)
        val socialType = kotlinAuthRequest.getSocialType()

        if (existAccount != null) {
            // 로그인
            val tokenResponse =
                login(KotlinAccountLoginRequest(account = existAccount, suffix = suffix).toLoginRequest())
            return KotlinLoginResponse(
                tokenResponse = tokenResponse,
                httpStatus = HttpStatus.OK
            )
        } else {
            val kotlinMiddleAccount = KotlinMiddleAccount(
                email = email,
                providerEmail = providerEmail,
                socialType = socialType,
            )
            // 회원가입
            val signTokenResponse = jwtProvider.createSignTokenDto(kotlinMiddleAccount)
            return KotlinLoginResponse(
                tokenResponse = signTokenResponse,
                httpStatus = HttpStatus.CREATED
            )
        }
    }

    @Transactional
    fun socialSignUp(signTokenRequest: SignTokenRequest): TokenResponse {
        val signKey = jwtProvider.getSignKey(signTokenRequest.signToken)
        checkSignUp(signKey)
        val nickname = nicknameRandom()
        duplicateEmailAndNickName(signKey.email, nickname)
        val account = KotlinAccount(
            email = signKey.email,
            nickname = nickname,
            password = passwordEncoder.encode(signKey.email + suffix),
            provider = signKey.socialType,
            providerEmail = signKey.providerEmail,
            role = KotlinRole.USER,
            accountStatus = KotlinAccountStatus.NORMAL
        )
        val saveAccount = kotlinAccountRepository.save(account)
        saveAccount.updateLastLoginDate()

        val initBookmark = createInitBookmark(saveAccount)
        saveAccount.updateBookmarks(initBookmark)

        val tokenResponse = login(KotlinAccountLoginRequest(account = saveAccount, suffix = suffix).toLoginRequest())

        return TokenResponse(
            accessToken = tokenResponse.accessToken,
            refreshToken = tokenResponse.refreshToken
        )
    }

    fun login(loginRequest: KotlinLoginRequest): TokenResponse {
        val email = loginRequest.email
        val account = getAccountByEmail(email)
        checkSuspended(account)
        account.updateLastLoginDate()
        return getTokenDto(loginRequest)
    }

    @Transactional
    fun checkSuspended(account: KotlinAccount) {
        val suspendedAccount = getSuspendedAccount(account) ?: return
        val isAccountExpired = LocalDateTime.now().isAfter(suspendedAccount.endedSuspendedAt)
        when (account.accountStatus) {
            KotlinAccountStatus.SUSPENDED -> {
                if (isAccountExpired) {
                    account.updateAccountStatus(KotlinAccountStatus.NORMAL)
                } else {
                    throw RuntimeException(KotlinErrorCode.SUSPENDED_ACCOUNT.errorMessage)
                }
            }

            KotlinAccountStatus.DELETED -> throw RuntimeException(KotlinErrorCode.DELETED_ACCOUNT.errorMessage)
            else -> return
        }
    }

    private fun checkRole(roleManager: String, authority: String) {
        if (roleManager != authority) {
            throw RuntimeException(KotlinErrorCode.ROLE_ACCESS_ERROR.errorMessage)
        }
    }

    fun basicLogin(loginRequest: KotlinLoginRequest, requiredRole: Role): TokenResponse {
        val authenticationToken = loginRequest.toAuthentication()
        val authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken)
        for (grantedAuthority in authentication.authorities) {
            val authority = grantedAuthority.authority
            checkRole(requiredRole.name, authority)
        }
        return getTokenDto(loginRequest)
    }

    fun adminLogin(loginRequestDto: KotlinLoginRequest): TokenResponse {
        return basicLogin(loginRequestDto, Role.ADMIN)
    }

    fun managerLogin(loginRequestDto: KotlinLoginRequest): TokenResponse {
        return basicLogin(loginRequestDto, Role.MANAGER)
    }


    @Transactional
    fun createManager(account: KotlinAccount, kotlinManagerInfoRequest: KotlinManagerInfoRequest): KotlinMessageResponse {
        val admin = account
        admin.role.checkAdmin()
        duplicateEmailAndNickName(kotlinManagerInfoRequest.email, kotlinManagerInfoRequest.nickname)
        val account = KotlinAccount(
            email = kotlinManagerInfoRequest.email,
            password = passwordEncoder.encode(kotlinManagerInfoRequest.password),
            nickname = kotlinManagerInfoRequest.nickname,
            role = KotlinRole.MANAGER,
            provider = KotlinSocialType.GOOGLE.name,
            providerEmail = "providerEmail",
            accountStatus = KotlinAccountStatus.NORMAL
        )
        val saveAccount = kotlinAccountRepository.save(account)
        saveAccount.updateBookmarks(createInitBookmark(account))
        return KotlinMessageResponse(message = "생성 완료", 200)
    }


    @Transactional
    fun logout(): KotlinMessageResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val loginAccount = authentication.name
        if (kotlinAccountRepository.existsByEmail(loginAccount) || kotlinRefreshTokenRepository.existsByKey(
                authentication.name
            )
        ) {
            val refreshToken = kotlinRefreshTokenRepository.findByKey(authentication.name)
            kotlinRefreshTokenRepository.delete(refreshToken)
        } else {
            throw RuntimeException(KotlinErrorCode.NOT_FOUND_MEMBER.errorMessage)
        }
        return KotlinMessageResponse(message = "로그아웃 성공", 200)
    }

    private fun createInitBookmark(saveAccount: KotlinAccount): MutableList<KotlinBookmark> {
        return getBookmarks(saveAccount, INIT_BOOKMARK_FOLDER_NAME, kotlinBookmarkRepository)
    }

    @Transactional
    fun getBookmarks(
        saveAccount: KotlinAccount,
        initBookmarkFolderName: String,
        bookmarkRepository: KotlinBookmarkRepository
    ): MutableList<KotlinBookmark> {
        val initBookmarkArticle = KotlinBookmark(
            account = saveAccount,
            folderName = initBookmarkFolderName,
            bookmarkType = KotlinBookmarkType.ARTICLE
        )
        val initBookmarkBookStore = KotlinBookmark(
            account = saveAccount,
            folderName = initBookmarkFolderName,
            bookmarkType = KotlinBookmarkType.BOOKSTORE
        )

        bookmarkRepository.save(initBookmarkArticle)
        bookmarkRepository.save(initBookmarkBookStore)
        return mutableListOf(initBookmarkArticle, initBookmarkBookStore)
    }


    private fun duplicateEmailAndNickName(email: String, nickname: String) {
        if (kotlinAccountRepository.existsByEmail(email)) {
            throw RuntimeException(KotlinErrorCode.EMAIL_DUPLICATION.errorMessage)
        }
        if (kotlinAccountRepository.existsByNickname(nickname)) {
            throw RuntimeException(KotlinErrorCode.NICKNAME_DUPLICATION.errorMessage)
        }
    }


    fun checkSignUp(signKey: KotlinSigningAccount) {
        if (signKey.email == null || signKey.socialType == null || signKey.providerEmail == null) {
            throw RuntimeException(KotlinErrorCode.INVALID_SIGN_TOKEN.errorMessage)
        }
    }

    @Transactional
    fun reissue(tokenRequestDto: TokenRequest): TokenResponse {
        if (!jwtProvider.validateToken(tokenRequestDto.refreshToken)) {
            throw JwtException(KotlinErrorCode.JWT_ERROR.errorMessage)
        }
        val authentication = jwtProvider.getAuthentication(tokenRequestDto.refreshToken)
        val refreshToken = kotlinRefreshTokenRepository.findByKey(authentication!!.name)
            ?: throw RuntimeException(KotlinErrorCode.NOT_FOUND_REFRESH_TOKEN.errorMessage)

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
            throw RuntimeException(KotlinErrorCode.NOT_FOUND_REFRESH_TOKEN.errorMessage)
        }
        if (refreshToken != tokenRequestDto.refreshToken) {
            throw RuntimeException(KotlinErrorCode.NOT_MATCH_REFRESH_TOKEN.errorMessage)
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


    fun getTokenDto(loginRequest: KotlinLoginRequest): TokenResponse {
        val authenticationToken = loginRequest.toAuthentication()
        val authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken)
        val tokenDto = jwtProvider.generateToken(authentication)
        val account = getAccountByEmail(loginRequest.email)
        val kotlinRefreshToken = KotlinRefreshToken(
            key = tokenDto.refreshToken,
            value = loginRequest.email,
            account = account
        )
        kotlinRefreshTokenRepository.save(kotlinRefreshToken)
        return tokenDto
    }


    fun getSocialIdWithAccessToken(data: KotlinAuthRequest): KotlinProviderIdAndEmail {
        return when (data.socialType) {
            KotlinSocialType.GOOGLE.name -> getGoogleIdAndEmail(data)
            KotlinSocialType.APPLE.name -> getAppleId(data.accessToken)
            else -> throw RuntimeException()
        }
    }

    private fun getGoogleIdAndEmail(data: KotlinAuthRequest): KotlinProviderIdAndEmail {
        val url = data.getSocialType().userInfoUrl
        val method = data.getSocialType().method
        val headers = setHeaders(data.accessToken)
        val request = HttpEntity<MultiValueMap<String, String>>(headers)
        val response: ResponseEntity<Map<String, Any>> = apiService.httpEntityPost(url, method, request, responseType)
        val userId = response.body?.get("sub") as String?
        val providerEmail = response.body?.get("email") as String?
        return KotlinProviderIdAndEmail(userId, providerEmail)
    }

    private fun getAppleAuthPublicKey(): KotlinAppleDto {
        val url = KotlinSocialType.APPLE.userInfoUrl
        val method = KotlinSocialType.APPLE.method
        val headers = HttpHeaders()
        val request = HttpEntity<MultiValueMap<String, String>>(headers)
        val response: ResponseEntity<KotlinAppleDto> =
            apiService.getAppleKeys(url, method, request, KotlinAppleDto::class.java)
        return response.body
    }

    private fun getAppleId(identityToken: String): KotlinProviderIdAndEmail {
        val appleKeyStorage = getAppleAuthPublicKey()
        try {
            val headerToken = identityToken.substring(0, identityToken.indexOf("."))
            val header: Map<String, String> = ObjectMapper().readValue(
                String(Base64.getDecoder().decode(headerToken), StandardCharsets.UTF_8),
                Map::class.java
            ) as Map<String, String>

            val key: KotlinAppleDto.AppleKey =
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
            return KotlinProviderIdAndEmail(subject, email)
        } catch (e: Exception) {
            throw RuntimeException(KotlinErrorCode.APPLE_LOGIN_ERROR.errorMessage)
        }
    }

    fun setHeaders(accessToken: String): HttpHeaders {
        val headers = HttpHeaders()
        headers["Authorization"] = "Bearer $accessToken"
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        return headers
    }

    fun getAccountByEmail(email: String): KotlinAccount {
        return kotlinAccountRepository.findByEmail(email)
            ?: throw RuntimeException(KotlinErrorCode.NOT_FOUND_MEMBER.errorMessage)
    }

    fun getSuspendedAccount(account: KotlinAccount): KotlinSuspendedAccount? {
        return kotlinSuspendedAccountRepository.findByAccount(account)
    }

    private companion object {
        const val INIT_BOOKMARK_FOLDER_NAME = "모아보기"
    }
}