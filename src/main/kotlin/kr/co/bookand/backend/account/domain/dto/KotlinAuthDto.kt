package kr.co.bookand.backend.account.domain.dto

import io.swagger.annotations.ApiModelProperty
import kr.co.bookand.backend.account.domain.SocialType

data class KotlinAuthRequest(
    @ApiModelProperty(value = "소셜 액세스 토큰", example = "ya29.a0Aa4xrXNXkiDBMm7MtSneVejzvupPun8S8EHorgvrt-nlCNy83PA9TI")
    val accessToken: String,
    @ApiModelProperty(value = "소셜타입", example = "GOOGLE/APPLE")
    val socialType: String,
    @ApiModelProperty(value = "소셜해당아이디", example = "요청 값이 아님")
    var id: String
) {
    fun insertId(id: String) {
        this.id = id
    }

    private fun getSocialType(): SocialType {
        return if (socialType.equals(SocialType.GOOGLE.name, ignoreCase = true)) {
            SocialType.GOOGLE
        } else {
            SocialType.APPLE
        }
    }

    fun extraEmail(): String {
        return if (getSocialType() == SocialType.GOOGLE) {
            "$id@google.com"
        } else {
            "$id@apple.com"
        }
    }
}

data class KotlinProviderIdAndEmail(
    val userId: String,
    val email: String
)

data class KotlinSigningAccount(
    val email: String,
    val providerEmail: String,
    val socialType: String
)

data class KotlinMiddleAccount(
    val email: String,
    val providerEmail: String,
    val socialType: SocialType
)