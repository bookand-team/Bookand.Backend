package kr.co.bookand.backend.common.exception;

import lombok.*;

public record ApiErrorResponse (
    String code,
    String message
) {
    @Builder
    public ApiErrorResponse{}
}
