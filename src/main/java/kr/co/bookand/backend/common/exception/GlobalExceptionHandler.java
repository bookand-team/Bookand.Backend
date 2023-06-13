package kr.co.bookand.backend.common.exception;

import kr.co.bookand.backend.account.exception.AccountException;
import kr.co.bookand.backend.article.exception.ArticleException;
import kr.co.bookand.backend.bookmark.exception.BookmarkException;
import kr.co.bookand.backend.bookstore.exception.BookStoreException;
import kr.co.bookand.backend.config.jwt.exception.JavaJwtException;
import kr.co.bookand.backend.policy.exception.PolicyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;
import java.security.SignatureException;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ApiErrorResponse> handle(AccountException ex) {
        log.error("handleException {} : {}", ex.errorCode.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ex.errorCode.getErrorCode())
                .message(ex.errorCode.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ex.errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(BookStoreException.class)
    public ResponseEntity<ApiErrorResponse> handle(BookStoreException ex) {
        log.error("handleException {} : {}", ex.errorCode.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ex.errorCode.getErrorCode())
                .message(ex.errorCode.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ex.errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(ArticleException.class)
    public ResponseEntity<ApiErrorResponse> handle(ArticleException ex) {
        log.error("handleException {} : {}", ex.errorCode.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ex.errorCode.getErrorCode())
                .message(ex.errorCode.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ex.errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(BookmarkException.class)
    public ResponseEntity<ApiErrorResponse> handle(BookmarkException ex) {
        log.error("handleException {} : {}", ex.errorCode.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ex.errorCode.getErrorCode())
                .message(ex.errorCode.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ex.errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(PolicyException.class)
    public ResponseEntity<ApiErrorResponse> handle(PolicyException ex) {
        log.error("handleException {} : {}", ex.errorCode.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ex.errorCode.getErrorCode())
                .message(ex.errorCode.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ex.errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(JavaJwtException.class)
    public ResponseEntity<ApiErrorResponse> handle(JavaJwtException ex) {
        log.error("GlobalExceptionHandler JwtException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ex.errorCode.getErrorCode())
                .message(ex.errorCode.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.JWT_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handle(Exception ex) {
        log.warn("GlobalExceptionHandler Exception {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ErrorCode.INTER_SERVER_ERROR.getErrorCode())
                .message(ErrorCode.INTER_SERVER_ERROR.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiErrorResponse> handle(IOException ex) {
        log.error("GlobalExceptionHandler IOException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ErrorCode.INTER_SERVER_ERROR.getErrorCode())
                .message(ErrorCode.INTER_SERVER_ERROR.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handle(HttpRequestMethodNotSupportedException ex) {
        log.error("GlobalExceptionHandler HttpRequestMethodNotSupportedException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ErrorCode.INTER_SERVER_ERROR.getErrorCode())
                .message(ErrorCode.INTER_SERVER_ERROR.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiErrorResponse> handle(HttpClientErrorException ex) {
        log.error("GlobalExceptionHandler HttpClientErrorException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ErrorCode.HTTP_CLIENT_ERROR.getErrorCode())
                .message(ErrorCode.HTTP_CLIENT_ERROR.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.HTTP_CLIENT_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handle(ValidationException ex) {
        log.error("GlobalExceptionHandler ValidationException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ErrorCode.INPUT_VALID_ERROR.getErrorCode())
                .message(ErrorCode.INPUT_VALID_ERROR.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.INPUT_VALID_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> validationException(final ConstraintViolationException ex) {
        log.warn("GlobalExceptionHandler ConstraintViolationException {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        String message = ex.getConstraintViolations().iterator().next().getMessage();
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ErrorCode.INPUT_VALID_ERROR.getErrorCode())
                .message(ErrorCode.INPUT_VALID_ERROR.getErrorMessage())
                .log(message)
                .build();
        return ResponseEntity.status(ErrorCode.INPUT_VALID_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handle(IllegalArgumentException ex) {
        log.error("GlobalExceptionHandler IllegalArgumentException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ErrorCode.INPUT_VALID_ERROR.getErrorCode())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.INPUT_VALID_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiErrorResponse> handle(SignatureException ex) {
        log.error("GlobalExceptionHandler SignatureException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ErrorCode.JWT_ERROR_SIGNATURE.getErrorCode())
                .message(ErrorCode.JWT_ERROR_SIGNATURE.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.JWT_ERROR_SIGNATURE.getHttpStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handle(RuntimeException ex) {
        log.error("GlobalExceptionHandler RuntimeException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder().code(ErrorCode.INTER_SERVER_ERROR.getErrorCode()).message(ex.getMessage()).build();
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiErrorResponse> handle(NullPointerException ex) {
        log.error("GlobalExceptionHandler NullPointerException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ErrorCode.INTER_SERVER_ERROR.getErrorCode())
                .message(ErrorCode.INTER_SERVER_ERROR.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> handle(NoSuchElementException ex) {
        log.error("GlobalExceptionHandler NoSuchElementException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ErrorCode.INTER_SERVER_ERROR.getErrorCode())
                .message(ErrorCode.INTER_SERVER_ERROR.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ApiErrorResponse> handle(UnsupportedOperationException ex) {
        log.error("GlobalExceptionHandler UnsupportedOperationException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ErrorCode.INTER_SERVER_ERROR.getErrorCode())
                .message(ErrorCode.INTER_SERVER_ERROR.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handle(BadCredentialsException ex) {
        log.error("GlobalExceptionHandler BadCredentialsException {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(ErrorCode.JWT_ERROR.getErrorCode())
                .message(ErrorCode.JWT_ERROR.getErrorMessage())
                .log(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.JWT_ERROR.getHttpStatus()).body(response);
    }
}
