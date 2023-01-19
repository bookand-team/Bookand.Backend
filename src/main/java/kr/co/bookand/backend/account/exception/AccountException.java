package kr.co.bookand.backend.account.exception;

import kr.co.bookand.backend.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountException extends RuntimeException {
    public ErrorCode errorCode;

    public AccountException(ErrorCode errorCode, Object exception) {
        super(exception != null ? errorCode.getMessage() : exception.toString());
        this.errorCode = errorCode;
    }
}
