package kr.co.bookand.backend.config.jwt

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)

data class SignTokenRequest(
    val signToken: String
)

data class TokenRequest(
    val refreshToken: String
)