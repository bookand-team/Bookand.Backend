package kr.co.bookand.backend.policy.exception;

import kr.co.bookand.backend.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PolicyException extends RuntimeException {
    public ErrorCode errorCode;

    public PolicyException(ErrorCode errorCode, Object exception) {
        super(exception != null ? errorCode.getMessage() : exception.toString());
        this.errorCode = errorCode;
    }
}
