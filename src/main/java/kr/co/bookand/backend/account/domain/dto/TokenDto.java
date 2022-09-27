package kr.co.bookand.backend.account.domain.dto;

import kr.co.bookand.backend.account.domain.dto.AuthDto;
import kr.co.bookand.backend.common.CodeStatus;
import kr.co.bookand.backend.common.Message;
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

    public AuthDto.TokenMessage toTokenMessage(String message, CodeStatus codeStatus) {
        return AuthDto.TokenMessage.builder()
                .message(Message.builder().msg(message).status(codeStatus).build())
                .data(this)
                .build();
    }
}