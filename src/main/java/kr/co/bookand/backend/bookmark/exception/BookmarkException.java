package kr.co.bookand.backend.bookmark.exception;

import kr.co.bookand.backend.common.exception.ErrorCode;

public class BookmarkException extends RuntimeException {
    public ErrorCode errorCode;

    public BookmarkException(ErrorCode errorCode, Object exception) {
        super(exception != null ? errorCode.getMessage() : exception.toString());
        this.errorCode = errorCode;
    }
}
