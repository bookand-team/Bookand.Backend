package kr.co.bookand.backend.common.slack

data class RequestInfo (
    val requestUrl: String,
    val method: String,
    val remoteAddr: String,
)