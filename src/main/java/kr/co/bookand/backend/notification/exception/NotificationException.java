package kr.co.bookand.backend.notification.exception;

import kr.co.bookand.backend.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationException extends RuntimeException {
    public ErrorCode errorCode;

    public NotificationException(ErrorCode errorCode, Object exception) {
        super(exception != null ? errorCode.getMessage() : exception.toString());
        this.errorCode = errorCode;
    }
}
