package kr.co.bookand.backend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND("COMMON-ERR-404","PAGE NOT FOUND"),
    INTER_SERVER_ERROR("COMMON-ERR-500","INTER SERVER ERROR"),
    NOT_FOUND_MEMBER("MEMBER-ERR-100","NOT FOUND MEMBER"),
    EMAIL_DUPLICATION("MEMBER-ERR-300","EMAIL DUPLICATED"),
    NICKNAME_DUPLICATION("MEMBER-ERR-301","NICKNAME DUPLICATED"),
    NOT_ROLE_MEMBER("MEMBER-ERR-302","NOT AUTHENTICATION ROLE"),
    JWT_ERROR("JWT-ERR-100","JWT EXCEPTION"),
    NOT_FOUND_POLICY("POLICY-ERR-100", "NOT FOUND POLICY"),
    NOT_FOUND_BOOKSTORE("BOOKSTORE-ERR-100", "NOT FOUND BOOKSTORE"),
    GOOGLE_LOGIN_ERROR("MEMBER-ERROR-200", "GOOGLE_LOGIN_ERROR"),
    APPLE_LOGIN_ERROR("MEMBER-ERR-201", "APPLE_LOGIN_ERROR")
    ;

    private String errorCode;
    private String message;
}