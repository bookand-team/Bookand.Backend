package kr.co.bookand.backend.account.domain.dto;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.Role;
import kr.co.bookand.backend.account.domain.SocialType;
import kr.co.bookand.backend.common.CodeStatus;
import kr.co.bookand.backend.common.Message;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthDto {

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class AuthRequest {
        private String socialType;
        private String accessToken;
        private String id;

        public void insertId(String id) {
            this.id = id;
        }

        public SocialType getSocialType() {
            if (socialType.equalsIgnoreCase(SocialType.GOOGLE.name())) {
                return SocialType.GOOGLE;
            } else {
                return SocialType.APPLE;
            }
        }

        public String extraEmail() {
            if (getSocialType().equals(SocialType.GOOGLE)) {
                return id + "@google.com";
            } else {
                return id + "@apple.com";
            }
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class AuthResponse {
        private String process;
        private Object token;

        public AuthResponseMessage toAuthResponseMessage(String msg) {
            return AuthResponseMessage.builder()
                    .message(Message.builder()
                            .status(CodeStatus.SUCCESS)
                            .msg(msg)
                            .build())
                    .data((AuthResponse) token)
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class AuthResponseMessage {
        private Message message;
        private AuthResponse data;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class TokenMessage {
        private Message message;
        private TokenDto data;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MiddleAccount{
        private String email;
        private SocialType socialType;

        public Account toAccount(PasswordEncoder passwordEncoder, String suffix, String nickname) {
            return Account.builder()
                    .email(email)
                    .provider(socialType.toString())
                    .password(passwordEncoder.encode(email + suffix))
                    .role(Role.USER)
                    .nickname(nickname)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class LoginRequest {
        private String refreshToken;

    }


}