package kr.co.bookand.backend.config.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import kr.co.bookand.backend.account.domain.Role
import kr.co.bookand.backend.account.domain.dto.MiddleAccount
import kr.co.bookand.backend.account.domain.dto.SigningAccount
import kr.co.bookand.backend.config.security.PrincipalDetailService
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
    private val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray())
    fun createSignTokenDto(middleAccount: MiddleAccount): SignTokenRequest {
        val now = Date().time
        val accessTokenExpiresIn: Date = Date(now + accessTokenExpireTime)
        val signToken = Jwts.builder()
            .setSubject(middleAccount.email + "_" + middleAccount.providerEmail + "_" + middleAccount.socialType)
            .claim("sign", Role.USER)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
        return SignTokenRequest(
            signToken = signToken
        )
    }

    fun getSignKey(signToken: String): SigningAccount {
        val body = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(signToken)
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
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            ?: return false
        return claims.body.expiration.after(Date())
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