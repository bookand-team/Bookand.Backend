package kr.co.bookand.backend.config.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import kr.co.bookand.backend.account.dto.MiddleAccount
import kr.co.bookand.backend.account.dto.SignTokenRequest
import kr.co.bookand.backend.account.dto.SigningAccount
import kr.co.bookand.backend.account.dto.TokenResponse
import kr.co.bookand.backend.account.model.Role
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.config.jwt.exception.JwtException
import kr.co.bookand.backend.config.security.PrincipalDetailService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.stream.Collectors


@Component
class JwtProvider(
    private val principalDetailService: PrincipalDetailService,

    @Value("\${jwt.secret}")
    private val secretKey: String,

    @Value("\${jwt.accessTokenExpireTime}")
    private val accessTokenExpireTime: Long,

    @Value("\${jwt.refreshTokenExpireTime}")
    private val refreshTokenExpireTime: Long
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private lateinit var signToken: String
    private val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray())
    fun createSignTokenDto(middleAccount: MiddleAccount): SignTokenRequest {
        val now = Date().time
        val accessTokenExpiresIn = Date(now + accessTokenExpireTime)
        signToken = Jwts.builder()
            .setSubject(middleAccount.email + "_" + middleAccount.providerEmail + "_" + middleAccount.socialType)
            .claim("sign", Role.USER)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
        return SignTokenRequest(
            signToken = signToken
        )
    }

    fun getSignKey(signTokenRequest: SignTokenRequest): SigningAccount {
        val body = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(signTokenRequest.signToken)
            .body
        val email = body.subject
        val split = email.split("_")
        return SigningAccount(split[0], split[1], split[2])
    }

    fun generateToken(authentication: Authentication): TokenResponse {
        val authenticate = authentication.authorities.stream()
            .map { obj -> obj.authority }
            .collect(Collectors.joining(","))

        val now = Date().time
        val accessTokenExpiresIn = Date(now + accessTokenExpireTime)

        val accessToken = Jwts.builder()
            .setSubject(authentication.name)
            .claim(TokenInfo.authoritiesKey, authenticate)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        val refreshToken = Jwts.builder()
            .setExpiration(Date(now + refreshTokenExpireTime))
            .setSubject(authentication.name)
            .claim(TokenInfo.authoritiesKey, authenticate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun validateToken(token: String?): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            true
        } catch (e: SignatureException) {
            log.warn("잘못된 JWT 서명입니다.")
            throw JwtException(ErrorCode.JWT_ERROR_SIGNATURE, e.message?:"")
        } catch (e: MalformedJwtException) {
            log.warn("잘못된 JWT 서명입니다.")
            throw JwtException(ErrorCode.JWT_ERROR_SIGNATURE, e.message?:"")
        } catch (e: ExpiredJwtException) {
            log.warn("만료된 JWT 토큰입니다. ")
            throw JwtException(ErrorCode.JWT_ERROR_EXPIRED, e.message?:"")
        } catch (e: UnsupportedJwtException) {
            log.warn("지원되지 않는 JWT 토큰입니다. ")
            throw JwtException(ErrorCode.JWT_ERROR_UNSUPPORTED, e.message?:"")
        } catch (e: IllegalArgumentException) {
            log.warn("JWT 토큰이 잘못되었습니다. ")
            throw JwtException(ErrorCode.JWT_ERROR_ILLEGAL, e.message?:"")
        }
    }

    fun getAuthentication(token: String): Authentication? {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        val principal = principalDetailService.loadUserByUsername(claims.subject)
        val authorities: Collection<SimpleGrantedAuthority> =
            claims[TokenInfo.authoritiesKey].toString().split(",").stream()
                .map { obj -> SimpleGrantedAuthority(obj) }.collect(Collectors.toList())
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }
}