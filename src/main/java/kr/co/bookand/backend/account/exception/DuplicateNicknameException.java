package kr.co.bookand.backend.account.exception;

import kr.co.bookand.backend.common.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class DuplicateNicknameException extends RuntimeException {
    private ErrorCode errorCode;

    public DuplicateNicknameException(Object exceptionObject) {
        super("nickname 중복입니다");
        log.warn("{}" , exceptionObject);
        this.errorCode = ErrorCode.NICKNAME_DUPLICATION;
    }
}