package kr.co.bookand.backend.config.jwt.exception

data class ApiErrorResponse(
    val code: String,
    val message: String,
    val log: String
)
