package kr.co.bookand.backend.config.jwt;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TokenInfo {
    public static final String AUTHORITIES_KEY = "AUTH";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TYPE = "BEARER";
    public static final int START_TOKEN_LOCATION = 7;
    public static final Long ACCESS_TOKEN_EXPIRE_TIME = 1000*60*30L; //30min
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000*60*60*24*7L; // 1week
}
