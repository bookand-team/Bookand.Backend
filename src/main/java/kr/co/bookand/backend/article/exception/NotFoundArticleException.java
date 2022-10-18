package kr.co.bookand.backend.article.exception;

import kr.co.bookand.backend.common.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class NotFoundArticleException extends RuntimeException {
    private ErrorCode errorCode;

    public NotFoundArticleException(Object exceptionObject) {
        super("서점 정보를 찾을 수 없습니다");
        log.info("{}", exceptionObject);
        this.errorCode = ErrorCode.NOT_FOUND_BOOKSTORE;
    }
}
