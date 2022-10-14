package kr.co.bookand.backend.config.jwt;

import kr.co.bookand.backend.common.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class JwtException extends RuntimeException {
    private ErrorCode errorCode;

    public JwtException() {
        super("JWT 예외처리");
        this.errorCode = ErrorCode.JWT_ERROR;
    }
}