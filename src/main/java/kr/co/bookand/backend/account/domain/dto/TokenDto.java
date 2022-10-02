package kr.co.bookand.backend.account.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.bookand.backend.common.Message;
import lombok.*;

import static kr.co.bookand.backend.account.domain.dto.AuthDto.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;

//    public TokenMessage toTokenMessage(String message, CodeStatus status){
//        return TokenMessage.builder()
//                .message(
//                        Message.builder()
//                                .msg(message)
//                                .status(status)
//                                .build()
//                )
//                .data(this).build();
//    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenRequestDto {
        private String refreshToken;
    }

}