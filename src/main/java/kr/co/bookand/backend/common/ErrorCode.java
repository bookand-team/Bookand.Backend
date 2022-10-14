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
    ;

    private String errorCode;
    private String message;
}