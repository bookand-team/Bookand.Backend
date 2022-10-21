package kr.co.bookand.backend.policy.exception;

import kr.co.bookand.backend.common.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class NotFoundContextException extends RuntimeException{
    private ErrorCode errorCode;

    public NotFoundContextException(Object exceptionObject) {
        super("약관 정보를 찾을 수 없습니다.");
        log.info("{}", exceptionObject);
        this.errorCode = ErrorCode.NOT_FOUND_POLICY;
    }
}
