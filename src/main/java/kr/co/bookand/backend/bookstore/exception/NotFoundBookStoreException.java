package kr.co.bookand.backend.bookstore.exception;

import kr.co.bookand.backend.common.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@Getter
public class NotFoundBookStoreException extends RuntimeException{
    private final ErrorCode errorCode;

    public NotFoundBookStoreException(Object exceptionObject) {
        super("서점 정보를 찾을 수 없습니다");
        log.info("{}", exceptionObject);
        this.errorCode = ErrorCode.NOT_FOUND_BOOKSTORE;
    }
}
