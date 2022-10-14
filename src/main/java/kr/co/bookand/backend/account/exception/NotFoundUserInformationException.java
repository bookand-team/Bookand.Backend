package kr.co.bookand.backend.account.exception;

import kr.co.bookand.backend.common.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class NotFoundUserInformationException extends RuntimeException {
    private ErrorCode errorCode;

    public NotFoundUserInformationException(Object exceptionObject) {
        super("유저 정보를 찾을 수 없습니다");
        log.info("{}", exceptionObject);
        this.errorCode = ErrorCode.NOT_FOUND_MEMBER;
    }
}