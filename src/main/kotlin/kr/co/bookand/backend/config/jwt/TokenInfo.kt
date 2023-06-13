package kr.co.bookand.backend.config.jwt

object TokenInfo {
    const val authoritiesKey: String = "AUTH"
    const val accessTokenExpireTime: Long = 1000 * 60 * 60 * 24 * 7 * 7L
    const val refreshTokenExpireTime: Long = 1000 * 60 * 60 * 24 * 7 * 100L
    const val AUTHORITIES_KEY = "AUTH"
    const val AUTHORIZATION_HEADER = "Authorization"
    const val BEARER_TYPE = "Bearer"
    const val START_TOKEN_LOCATION = 7
}