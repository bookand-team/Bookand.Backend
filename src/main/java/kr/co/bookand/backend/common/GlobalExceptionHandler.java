package kr.co.bookand.backend.common;

import kr.co.bookand.backend.config.jwt.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ValidationException;
import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiErrorResponse> handle(IOException ex){
        log.error("GlobalExceptionHandler",ex);
        ApiErrorResponse response = new ApiErrorResponse(ErrorCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiErrorResponse> handle(JwtException ex){
        log.error("GlobalExceptionHandler",ex);
        ApiErrorResponse response = new ApiErrorResponse(ErrorCode.JWT_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handle(HttpRequestMethodNotSupportedException ex){
        log.error("GlobalExceptionHandler",ex);
        ApiErrorResponse response = new ApiErrorResponse(ErrorCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiErrorResponse> handle(HttpClientErrorException ex){
        log.error("GlobalExceptionHandler",ex);
        ApiErrorResponse response = new ApiErrorResponse(ErrorCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handle(ValidationException ex){
        log.error("handleException",ex);
        ApiErrorResponse response = new ApiErrorResponse(ErrorCode.JWT_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
