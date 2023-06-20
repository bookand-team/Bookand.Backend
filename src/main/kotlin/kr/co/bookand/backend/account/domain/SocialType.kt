package kr.co.bookand.backend.account.domain

import org.springframework.http.HttpMethod

enum class SocialType(
    val socialName: String,
    val userInfoUrl: String,
    val method: HttpMethod
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
}
