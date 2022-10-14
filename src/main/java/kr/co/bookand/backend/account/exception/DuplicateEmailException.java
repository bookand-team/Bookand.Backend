package kr.co.bookand.backend.account.exception;

import kr.co.bookand.backend.common.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class DuplicateEmailException extends RuntimeException {
    private ErrorCode errorCode;

    public DuplicateEmailException(Object exceptionObject) {
        super("이메일이 중복입니다.");
        log.warn("{}", exceptionObject);
        this.errorCode = ErrorCode.EMAIL_DUPLICATION;
    }
}