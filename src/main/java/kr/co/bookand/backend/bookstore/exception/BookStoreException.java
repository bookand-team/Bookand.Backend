package kr.co.bookand.backend.bookstore.exception;

import kr.co.bookand.backend.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookStoreException extends RuntimeException {
    public ErrorCode errorCode;

    public BookStoreException(ErrorCode errorCode, Object exception) {
        super(exception != null ? errorCode.getMessage() : exception.toString());
        this.errorCode = errorCode;
    }
}
