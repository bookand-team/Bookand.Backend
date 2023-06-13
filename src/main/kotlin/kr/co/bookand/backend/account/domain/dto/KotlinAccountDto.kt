package kr.co.bookand.backend.account.domain.dto

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.common.domain.dto.PageResponse

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

data class KotlinManagerRequest(
    val id: Long,
    val email: String,
    val password: String,
    val nickname: String,
)

data class KotlinNickNameResponse(
    val nickname: String
)

data class KotlinAccountListResponse(
    val memberList : PageResponse<KotlinAccountInfoResponse>
)
