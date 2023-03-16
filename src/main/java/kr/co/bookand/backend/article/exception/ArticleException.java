package kr.co.bookand.backend.article.exception;

import kr.co.bookand.backend.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArticleException extends RuntimeException {
    public ErrorCode errorCode;

    public ArticleException(ErrorCode errorCode, Object exception) {
        super(exception != null ? errorCode.getErrorLog() : exception.toString());
        this.errorCode = errorCode;
    }
}
