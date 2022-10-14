package kr.co.bookand.backend.account.exception;

import kr.co.bookand.backend.common.ApiErrorResponse;
import kr.co.bookand.backend.common.ApiResponse;
import kr.co.bookand.backend.common.ErrorCode;
import kr.co.bookand.backend.common.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AccountExceptionHandler {

    @ExceptionHandler(NotFoundUserInformationException.class)
    public ResponseEntity<ApiErrorResponse> handle(NotFoundUserInformationException ex){
        log.error("handleException {} : {}",ex.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse(ErrorCode.NOT_FOUND_MEMBER);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiErrorResponse> handle(DuplicateEmailException ex){
        log.error("handleException {} : {}",ex.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse(ErrorCode.EMAIL_DUPLICATION);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<ApiErrorResponse> handle(DuplicateNicknameException ex){
        log.error("handleException {} : {}",ex.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse(ErrorCode.NICKNAME_DUPLICATION);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotRoleUserException.class)
    public ResponseEntity<ApiErrorResponse> handle(NotRoleUserException ex){
        log.error("handleException {} : {}",ex.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse(ErrorCode.NICKNAME_DUPLICATION);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
