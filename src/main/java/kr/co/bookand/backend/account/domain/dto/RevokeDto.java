package kr.co.bookand.backend.account.domain.dto;


import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RevokeDto {


    public record RevokeReasonRequest(
            @NotNull
            @NotEmpty
            String revokeType,

            String reason,
            @NotNull
            @NotEmpty
            String socialAccessToken
    ) {
        @Builder
        public RevokeReasonRequest {
        }
    }
}
