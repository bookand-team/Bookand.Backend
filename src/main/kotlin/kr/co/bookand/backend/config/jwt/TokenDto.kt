package kr.co.bookand.backend.config.jwt

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)

data class SignTokenResponse(
    val signToken: String
)