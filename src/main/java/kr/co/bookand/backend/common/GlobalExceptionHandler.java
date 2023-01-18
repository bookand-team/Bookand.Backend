package kr.co.bookand.backend.common;

import kr.co.bookand.backend.account.exception.AccountException;
import kr.co.bookand.backend.article.exception.ArticleException;
import kr.co.bookand.backend.bookstore.exception.BookStoreException;
import kr.co.bookand.backend.config.jwt.JwtException;
import kr.co.bookand.backend.policy.exception.PolicyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ApiErrorResponse> handle(AccountException ex){
        log.error("handleException {} : {}",ex.errorCode.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder().code(ex.errorCode.getErrorCode()).message(ex.getMessage()).build();
        return ResponseEntity.status(ex.errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(BookStoreException.class)
    public ResponseEntity<ApiErrorResponse> handle(BookStoreException ex){
        log.error("handleException {} : {}",ex.errorCode.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder().code(ex.errorCode.getErrorCode()).message(ex.getMessage()).build();
        return ResponseEntity.status(ex.errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(ArticleException.class)
    public ResponseEntity<ApiErrorResponse> handle(ArticleException ex){
        log.error("handleException {} : {}",ex.errorCode.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder().code(ex.errorCode.getErrorCode()).message(ex.getMessage()).build();
        return ResponseEntity.status(ex.errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(PolicyException.class)
    public ResponseEntity<ApiErrorResponse> handle(PolicyException ex){
        log.error("handleException {} : {}",ex.errorCode.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder().code(ex.errorCode.getErrorCode()).message(ex.getMessage()).build();
        return ResponseEntity.status(ex.errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception ex) {
        log.warn("GlobalExceptionHandler Exception {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder().code(ErrorCode.INTER_SERVER_ERROR.getErrorCode()).message(ex.getMessage()).build();
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiErrorResponse> handle(IOException ex){
        log.error("GlobalExceptionHandler IOException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder().code(ErrorCode.INTER_SERVER_ERROR.getErrorCode()).message(ex.getMessage()).build();
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiErrorResponse> handle(JwtException ex){
        log.error("GlobalExceptionHandler JwtException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder().code(ErrorCode.JWT_ERROR.getErrorCode()).message(ex.getMessage()).build();
        return ResponseEntity.status(ErrorCode.JWT_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handle(HttpRequestMethodNotSupportedException ex){
        log.error("GlobalExceptionHandler HttpRequestMethodNotSupportedException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder().code(ErrorCode.INTER_SERVER_ERROR.getErrorCode()).message(ex.getMessage()).build();
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiErrorResponse> handle(HttpClientErrorException ex){
        log.error("GlobalExceptionHandler HttpClientErrorException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder().code(ErrorCode.HTTP_CLIENT_ERROR.getErrorCode()).message(ex.getMessage()).build();
        return ResponseEntity.status(ErrorCode.HTTP_CLIENT_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handle(ValidationException ex){
        log.error("GlobalExceptionHandler ValidationException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder().code(ErrorCode.INPUT_VALID_ERROR.getErrorCode()).message(ex.getMessage()).build();
        return ResponseEntity.status(ErrorCode.INPUT_VALID_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiErrorResponse> validationException(final ConstraintViolationException ex) {
        log.warn("GlobalExceptionHandler ConstraintViolationException {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        String message = ex.getConstraintViolations().iterator().next().getMessage();
        ApiErrorResponse response = ApiErrorResponse.builder().code(ErrorCode.INPUT_VALID_ERROR.getErrorCode()).message(message).build();
        return ResponseEntity.status(ErrorCode.INPUT_VALID_ERROR.getHttpStatus()).body(response);
    }
}
