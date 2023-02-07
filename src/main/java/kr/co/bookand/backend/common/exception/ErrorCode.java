package kr.co.bookand.backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND("COMMON-ERR-404","PAGE NOT FOUND", 404),
    INPUT_VALID_ERROR("COMMON-ERR-400","INPUT VALID ERROR", 400),
    INTER_SERVER_ERROR("COMMON-ERR-500","INTER SERVER ERROR", 400),
    HTTP_CLIENT_ERROR("COMMON-ERR-401", "HTTP CLIENT ERROR", 400),

    NOT_ROLE_MEMBER("MEMBER-ERR-302","NOT AUTHENTICATION ROLE", 403),
    NOT_FOUND_MEMBER("MEMBER-ERR-100","NOT FOUND MEMBER", 404),
    ROLE_ACCESS_ERROR("MEMBER-ERR-101","ROLE ACCESS ERROR", 403),
    EMAIL_DUPLICATION("MEMBER-ERR-300","EMAIL DUPLICATED", 400),
    NICKNAME_DUPLICATION("MEMBER-ERR-301","NICKNAME DUPLICATED", 400),
    APPLE_LOGIN_ERROR("MEMBER-ERR-201", "APPLE_LOGIN_ERROR", 400),
    GOOGLE_LOGIN_ERROR("MEMBER-ERROR-200", "GOOGLE_LOGIN_ERROR", 404),

    JWT_ERROR("JWT-ERR-100","JWT EXCEPTION", 401),
    JWT_ERROR_SIGNATURE("JWT-ERR-101","SIGNATURE JWT ERROR", 401),
    JWT_ERROR_EXPIRED("JWT-ERR-102","EXPIRED JWT ERROR", 401),
    JWT_ERROR_UNSUPPORTED("JWT-ERR-103","UNSUPPORTED JWT ERROR", 401),
    JWT_ERROR_ILLEGAL("JWT-ERR-104","ILLEGAL ARGUMENT JWT ERROR", 401),
    NOT_FOUND_REFRESH_TOKEN("REFRESH_TOKEN-ERR-100", "NOT FOUND REFRESH_TOKEN", 404),
    NOT_MATCH_REFRESH_TOKEN("REFRESH_TOKEN-ERR-101", "NOT MATCH REFRESH_TOKEN", 400),

    NOT_FOUND_POLICY("POLICY-ERR-100", "NOT FOUND POLICY", 404),
    ALREADY_EXIST_POLICY("POLICY-ERR-101", "ALREADY EXIST POLICY", 400),

    NOT_FOUND_BOOKSTORE("BOOKSTORE-ERR-100", "NOT FOUND BOOKSTORE", 404),
    DUPLICATE_BOOKSTORE_NAME("BOOKSTORE-ERR-101", "DUPLICATE BOOKSTORE NAME", 400),
    NOT_FOUND_BOOKSTORE_IMAGE("BOOKSTORE-ERR-102", "NOT FOUND BOOKSTORE IMAGE", 404),
    NOT_FOUND_ARTICLE_BOOKSTORE("ARTICLE_BOOKSTORE-ERR-100", "NOT FOUND ARTICLE BOOKSTORE", 404),

    NOT_FOUND_ARTICLE("ARTICLE-ERR-100", "NOT FOUND ARTICLE", 404),
    DUPLICATE_ARTICLE("ARTICLE-ERR-101", "DUPLICATE ARTICLE", 400),
    ;

    private String errorCode;
    private String message;
    private int httpStatus;
}