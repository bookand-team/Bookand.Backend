package kr.co.bookand.backend.account.domain.dto;

import lombok.*;


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
        private String refreshToken;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TokenResponse{
        private String accessToken;
        private String refreshToken;

    }

}