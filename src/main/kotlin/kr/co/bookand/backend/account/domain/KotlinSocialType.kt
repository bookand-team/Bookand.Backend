package kr.co.bookand.backend.account.domain

import org.springframework.http.HttpMethod

enum class KotlinSocialType(
    private val socialName: String,
    private val userInfoUrl: String,
    private val method: HttpMethod
) {
    GOOGLE(
        "google",
        "https://www.googleapis.com/oauth2/v3/userinfo",
        HttpMethod.GET
    ),

    APPLE(
        "apple",
        "https://appleid.apple.com/auth/keys",
        HttpMethod.GET
    );

    fun getMethod(): HttpMethod {
        return method
    }

    fun getSocialName(): String {
        return socialName
    }

    fun getUserInfoUrl(): String {
        return userInfoUrl
    }
}
