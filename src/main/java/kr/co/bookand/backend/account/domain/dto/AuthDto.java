package kr.co.bookand.backend.account.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.Role;
import kr.co.bookand.backend.account.domain.SocialType;
import kr.co.bookand.backend.common.domain.Message;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthDto {

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class AuthRequest {
        @ApiModelProperty(value = "소셜 액세스 토큰", example = "ya29.a0Aa4xrXNXkiDBMm7MtSneVejzvupPun8S8EHorgvrt-nlCNy83PA9TI")
        private String accessToken;
        @ApiModelProperty(value = "소셜타입", example = "GOOGLE/APPLE")
        private String socialType;
        @ApiModelProperty(value = "소셜해당아이디",example = "요청 값이 아님")
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
    public static class MiddleAccount{

        private String email;
        private String providerEmail;
        private SocialType socialType;

        public Account toAccount(PasswordEncoder passwordEncoder, String suffix, String nickname, String providerEmail) {
            return Account.builder()
                    .email(email)
                    .provider(socialType.toString())
                    .providerEmail(providerEmail)
                    .password(passwordEncoder.encode(email + suffix))
                    .role(Role.USER)
                    .nickname(nickname)
                    .build();
        }

        public Account toAdmin(PasswordEncoder passwordEncoder, CharSequence ADMIN_PASSWORD) {
            return Account.builder()
                    .email("admin")
                    .provider("admin")
                    .providerEmail("admin")
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .role(Role.ADMIN)
                    .nickname("admin")
                    .build();
        }

        public Account toManager(PasswordEncoder passwordEncoder, String email, String password, String nickname) {
            return Account.builder()
                    .email(email)
                    .provider("manager")
                    .providerEmail("manager")
                    .password(passwordEncoder.encode(password))
                    .role(Role.MANAGER)
                    .nickname(nickname)
                    .build();
        }

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
    public static class ProviderIdAndEmail{
        private String userId;
        private String email;

        public static ProviderIdAndEmail toProviderDto(String userId, String email) {
            return new ProviderIdAndEmail(userId, email);
        }
    }
}