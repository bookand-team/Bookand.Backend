package kr.co.bookand.backend.common;

import lombok.*;

public record ApiErrorResponse (
    String code,
    String message
) {
    @Builder
    public ApiErrorResponse{}
}
