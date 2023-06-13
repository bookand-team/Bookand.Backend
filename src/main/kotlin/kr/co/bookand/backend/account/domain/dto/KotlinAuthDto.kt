package kr.co.bookand.backend.account.domain.dto

import kr.co.bookand.backend.account.domain.SocialType

data class KotlinSigningAccount(
    val email : String,
    val providerEmail : String,
    val socialType : String
)

data class KotlinMiddleAccount(
    val email : String,
    val providerEmail : String,
    val socialType : SocialType
)