package kr.co.bookand.backend.bookstore.exception;

import kr.co.bookand.backend.common.ApiErrorResponse;
import kr.co.bookand.backend.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BookStoreExceptionHandler{

    @ExceptionHandler(NotFoundBookStoreException.class)
    public ResponseEntity<ApiErrorResponse> handle(NotFoundBookStoreException ex){
        log.error("handleException {} : {}",ex.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse(ErrorCode.NOT_FOUND_MEMBER);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}