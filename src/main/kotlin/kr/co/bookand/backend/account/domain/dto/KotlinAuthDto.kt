package kr.co.bookand.backend.account.domain.dto

import io.swagger.annotations.ApiModelProperty
import kr.co.bookand.backend.account.domain.*
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder

data class KotlinAuthRequest(
    @ApiModelProperty(value = "소셜 액세스 토큰", example = "ya29.a0Aa4xrXNXkiDBMm7MtSneVejzvupPun8S8EHorgvrt-nlCNy83PA9TI")
    val accessToken: String,
    @ApiModelProperty(value = "소셜타입", example = "GOOGLE/APPLE")
    val socialType: String,
    @ApiModelProperty(value = "소셜해당아이디", example = "요청 값이 아님")
    var id: String? = null,
) {
    fun insertId(id: String?) {
        if (id != null) {
            this.id = id
        }
    }

    fun getSocialType(): KotlinSocialType {
        return if (socialType.equals(KotlinSocialType.GOOGLE.name, ignoreCase = true)) {
            KotlinSocialType.GOOGLE
        } else {
            KotlinSocialType.APPLE
        }
    }

    fun extraEmail(): String {
        return if (getSocialType() == KotlinSocialType.GOOGLE) {
            "$id@google.com"
        } else {
            "$id@apple.com"
        }
    }
}

data class KotlinProviderIdAndEmail(
    val userId: String?,
    val email: String?
)

data class KotlinSigningAccount(
    val email: String,
    val providerEmail: String,
    val socialType: String
)

data class KotlinMiddleAccount(
    val email: String,
    val providerEmail: String?,
    val socialType: KotlinSocialType
){
    fun toAdminAccount(passwordEncoder: PasswordEncoder, adminPassword: CharSequence) : KotlinAccount{
       return KotlinAccount(
            email = email,
            password = passwordEncoder.encode(adminPassword),
            nickname = "admin",
            provider = socialType.socialName,
            providerEmail = "providerEmail",
            role = KotlinRole.ADMIN,
            accountStatus = KotlinAccountStatus.NORMAL
        )
    }
}

data class KotlinLoginRequest(
    @ApiModelProperty(value = "이메일", example = "bookand@example.com")
    var email: String,
    @ApiModelProperty(value = "비밀번호", example = "비밀번호")
    var password: String
){

    fun toAuthentication(): UsernamePasswordAuthenticationToken {
        return UsernamePasswordAuthenticationToken(email, password)
    }
}

data class KotlinLoginResponse(
    val tokenResponse: Any,
    val httpStatus: HttpStatus
)

data class KotlinAccountLoginRequest(
    val account : KotlinAccount,
    val suffix : String
){
    fun toLoginRequest() : KotlinLoginRequest {
        return KotlinLoginRequest(
            email = account.email,
            password = account.password + suffix
        )
    }
}