package kr.co.bookand.backend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND("COMMON-ERR-404","PAGE NOT FOUND"),
    INTER_SERVER_ERROR("COMMON-ERR-500","INTER SERVER ERROR"),
    EMAIL_DUPLICATION("MEMBER-ERR-400","EMAIL DUPLICATED"),
    ;

    private String errorCode;
    private String message;
}