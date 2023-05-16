package kr.co.bookand.backend.account.domain.dto

import kr.co.bookand.backend.account.domain.KotlinAccount

data class KotlinAccountRequest(
    val nickname: String,
    val profileImage: String
)

data class KotlinAccountInfoResponse(
    val id: Long,
    val email: String,
    val providerEmail: String,
    val nickname: String,
    val profileImage: String,
    val providerType: String,
    val lastLoginDate: String,
    val signUpDate: String,
    val accountStatus: String
){
    constructor(kotlinAccount: KotlinAccount) : this(
        id = kotlinAccount.id,
        email = kotlinAccount.email,
        providerEmail = kotlinAccount.providerEmail,
        nickname = kotlinAccount.nickname,
        profileImage = kotlinAccount.profileImage,
        providerType = kotlinAccount.provider,
        lastLoginDate = kotlinAccount.lastLoginDate.toString(),
        signUpDate = kotlinAccount.signUpDate.toString(),
        accountStatus = kotlinAccount.accountStatus.name
    )
}