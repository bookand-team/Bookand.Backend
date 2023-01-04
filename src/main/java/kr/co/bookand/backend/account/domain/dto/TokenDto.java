package kr.co.bookand.backend.account.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.http.HttpStatus;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;

    public TokenResponse toTokenDto() {
        return TokenDto.TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenRequest {
        @ApiModelProperty(value = "갱신토큰값",example = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NjQ2")
        private String refreshToken;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TokenResponse{
        @ApiModelProperty(value = "소셜 액세스 토큰", example = "ya29.a0Aa4xrXNXkiDBMm7MtSneVejzvupPun8S8EHorgvrt-nlCNy83PA9TI")
        private String accessToken;
        @ApiModelProperty(value = "갱신토큰값",example = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NjQ2")
        private String refreshToken;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginResponse {
        public TokenResponse tokenResponse;
        public HttpStatus httpStatus;
    }

}