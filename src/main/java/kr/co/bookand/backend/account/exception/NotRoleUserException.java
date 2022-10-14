package kr.co.bookand.backend.account.exception;

import kr.co.bookand.backend.common.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class NotRoleUserException extends RuntimeException {
    private ErrorCode errorCode;

    public NotRoleUserException(Object exceptionObject) {
        super("유저 권한이 올바르지 않습니다.");
        log.info("{}", exceptionObject);
        this.errorCode = ErrorCode.NOT_ROLE_MEMBER;
    }
}