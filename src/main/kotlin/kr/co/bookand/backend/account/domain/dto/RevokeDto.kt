package kr.co.bookand.backend.account.domain.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class RevokeReasonRequest(
    @NotNull
    @NotEmpty
    val revokeType: String,

    val reason: String,

    @NotNull
    @NotEmpty
    val socialAccessToken: String
)