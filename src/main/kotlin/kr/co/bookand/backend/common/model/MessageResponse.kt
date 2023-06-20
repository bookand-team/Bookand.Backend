package kr.co.bookand.backend.common.model

import com.fasterxml.jackson.annotation.JsonIgnore

data class MessageResponse(
    val message: String,
    @JsonIgnore
    val statusCode : Int
)