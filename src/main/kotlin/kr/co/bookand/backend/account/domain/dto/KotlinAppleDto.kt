package kr.co.bookand.backend.account.domain.dto


import java.util.List
import java.util.Optional

data class KotlinAppleDto(
    val id: String?,
    val token: String?,
    val keys: List<AppleKey>?
) {
    data class AppleKey(
        val kty: String?,
        val kid: String?,
        val use: String?,
        val alg: String?,
        val n: String?,
        val e: String?
    )

    fun getMatchedKeyBy(kid: String?, alg: String?): Optional<AppleKey> {
        return keys?.stream()
            ?.filter { key -> key.kid == kid && key.alg == alg }
            ?.findFirst() ?: Optional.empty()
    }
}

data class KotlinAppleCodeResponse(
    val access_token: String?,
    val expires_in: Int,
    val id_token: String?,
    val refresh_token: String?,
    val token_type: String?
)
