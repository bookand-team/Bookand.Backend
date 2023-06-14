package kr.co.bookand.backend.common.domain

import com.fasterxml.jackson.annotation.JsonIgnore

data class KotlinMessageResponse(
    val message: String,
    @JsonIgnore
    val statusCode : Int
)