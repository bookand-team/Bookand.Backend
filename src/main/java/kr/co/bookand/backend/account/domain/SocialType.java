package kr.co.bookand.backend.account.domain;

import org.springframework.http.HttpMethod;

public enum SocialType {
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


    private String socialName;
    private String userInfoUrl;
    private HttpMethod method;

    SocialType(String socialName, String userInfoUrl, HttpMethod method) {
        this.socialName = socialName;
        this.userInfoUrl = userInfoUrl;
        this.method = method;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getSocialName() {
        return socialName;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }
}