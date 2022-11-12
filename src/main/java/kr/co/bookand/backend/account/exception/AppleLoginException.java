package kr.co.bookand.backend.account.exception;

import kr.co.bookand.backend.common.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class AppleLoginException extends RuntimeException {
    private ErrorCode errorCode;

    public AppleLoginException(Object exceptionObject) {
        super("애플 로그인 오류.");
        log.warn("{}", exceptionObject);
        this.errorCode = ErrorCode.APPLE_LOGIN_ERROR;
    }
}