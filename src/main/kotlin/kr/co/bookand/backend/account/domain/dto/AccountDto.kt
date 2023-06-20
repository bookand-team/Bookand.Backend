package kr.co.bookand.backend.account.domain.dto

import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.common.PageResponse

data class AccountRequest(
    val nickname: String,
    val profileImage: String
)

data class AccountInfoResponse(
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
    constructor(account: Account) : this(
        id = account.id,
        email = account.email,
        providerEmail = account.providerEmail,
        nickname = account.nickname,
        profileImage = account.profileImage ?: "",
        providerType = account.provider,
        lastLoginDate = account.lastLoginDate.toString(),
        signUpDate = account.signUpDate.toString(),
        accountStatus = account.accountStatus.name
    )

    fun toLoginRequest(suffix : String) : LoginRequest {
        return LoginRequest(email = email, password = email + suffix)
    }
}

data class AccountListResponse(
    val memberList : PageResponse<AccountInfoResponse>
)

data class NicknameResponse(
    val nickname: String
)

data class ManagerInfoRequest(
    val id: Long,
    val email: String,
    val password: String,
    val nickname: String,
)