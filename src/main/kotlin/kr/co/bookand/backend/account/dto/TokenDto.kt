package kr.co.bookand.backend.account.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)

data class SignTokenRequest(
    var signToken: String
)

data class TokenRequest(
    val refreshToken: String
)