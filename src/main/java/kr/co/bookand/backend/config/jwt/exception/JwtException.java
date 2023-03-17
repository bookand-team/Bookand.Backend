package kr.co.bookand.backend.config.jwt.exception;

import kr.co.bookand.backend.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {
    public ErrorCode errorCode;

    public JwtException(ErrorCode errorCode, Object exception) {
        super(exception != null ? errorCode.getErrorLog() : exception.toString());
        this.errorCode = errorCode;
    }

    public String convertToJson(String ex)
    {
        return "{\"code\":\"" + errorCode.getErrorCode() + "\",\"log\":\"" + ex + "message\":\"" + errorCode.getErrorMessage() + "\"}";
    }
}