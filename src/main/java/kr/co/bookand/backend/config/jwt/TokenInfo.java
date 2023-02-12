package kr.co.bookand.backend.config.jwt;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TokenInfo {
    public static final String AUTHORITIES_KEY = "AUTH";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TYPE = "Bearer";
    public static final int START_TOKEN_LOCATION = 7;
}
