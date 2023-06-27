package kr.co.bookand.backend.account.dto

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.account.model.AccountStatus
import kr.co.bookand.backend.account.model.Role
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
) {
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

    fun toLoginRequest(suffix: String): LoginRequest {
        return LoginRequest(email = email, password = email + suffix)
    }
}

data class AccountDetailInfoResponse(
    val id: Long,
    val providerEmail: String,
    val nickname: String,
    val role: String,
    val signUpDate: String,
    val lastLoginDate: String,
    val accountStatus: String,
    val bookmarkedArticleList: Int,
    val bookmarkedBookstoreList: Int
) {
    constructor(account: Account) : this(
        id = account.id,
        providerEmail = account.providerEmail,
        nickname = account.nickname,
        role = account.role.name,
        signUpDate = account.signUpDate.toString(),
        lastLoginDate = account.lastLoginDate.toString(),
        accountStatus = account.accountStatus.name,
        bookmarkedArticleList = account.bookmarkList.sumOf { it.bookmarkedArticleList.size },
        bookmarkedBookstoreList = account.bookmarkList.sumOf { it.bookmarkedBookstoreList.size }
    )
}


data class AccountListResponse(
    val memberList: PageResponse<AccountDetailInfoResponse>
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

data class AccountStatusRequest(
    val accountStatus: AccountStatus,
    val role: Role,
    val reason: String
)

data class AccountIdResponse(
    val id: Long
)

data class AccountFilterRequest(
    val accountStatus: AccountStatus,
    val role: Role
)

data class AccountSearchRequest(
    val searchType: String,
    val searchKeyword: String
)