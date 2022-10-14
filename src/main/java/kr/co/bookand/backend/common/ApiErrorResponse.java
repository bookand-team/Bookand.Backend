package kr.co.bookand.backend.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiErrorResponse {

    private String message;
    private String code;

    public ApiErrorResponse(ErrorCode errorCode){
        this.message = errorCode.getMessage();
        this.code = errorCode.getErrorCode();
    }
}
