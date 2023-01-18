package kr.co.bookand.backend.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.co.bookand.backend.account.domain.dto.TokenDto;
import kr.co.bookand.backend.common.exception.ErrorCode;
import kr.co.bookand.backend.config.jwt.exception.JwtException;
import kr.co.bookand.backend.config.security.PrincipalDetailService;
import kr.co.bookand.backend.config.security.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenFactory {


    private PrincipalDetailService principalDetailService;
    private ObjectMapper objectMapper;

    @Value("${jwt.accessTokenExpireTime}")
    public long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${jwt.refreshTokenExpireTime}")
    public long REFRESH_TOKEN_EXPIRE_TIME;

    private final Key key;

    public TokenFactory(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        System.out.println("authorities = " + authorities);

        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(TokenInfo.AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .setSubject(authentication.getName())
                .claim(TokenInfo.AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .grantType(TokenInfo.BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if (claims.get(TokenInfo.AUTHORITIES_KEY) == null) throw new IllegalArgumentException("권한 정보가 없는 토큰입니다.");

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(TokenInfo.AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(principalDetails, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.");
            throw new JwtException(ErrorCode.JWT_ERROR_SIGNATURE, e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다. ");
            throw new JwtException(ErrorCode.JWT_ERROR_EXPIRED, e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다. ");
            throw new JwtException(ErrorCode.JWT_ERROR_UNSUPPORTED, e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 잘못되었습니다. ");
            throw new JwtException(ErrorCode.JWT_ERROR_ILLEGAL, e.getMessage());
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
