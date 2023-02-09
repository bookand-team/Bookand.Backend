package kr.co.bookand.backend.account.domain.dto;


import io.swagger.annotations.ApiModelProperty;
import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AccountDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MemberRequest {
        @ApiModelProperty(value = "이메일", example = "bookand@example.com")
        private String email;
        @ApiModelProperty(value = "비밀번호", example = "비밀번호")
        private String password;
        @ApiModelProperty(value = "닉네임", example = "bookand")
        private String nickname;
        @ApiModelProperty(value = "소셜타입", example = "GOOGLE/APPLE")
        private String socialType;

        public LoginRequest toLoginRequest() {
            return LoginRequest.builder()
                    .email(email)
                    .password(password)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberUpdateRequest {
        @ApiModelProperty(value = "닉네임", example = "bookand")
        private String nickname;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LoginRequest {
        @ApiModelProperty(value = "이메일", example = "bookand@example.com")
        private String email;
        @ApiModelProperty(value = "비밀번호", example = "비밀번호")
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email,password);
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MemberInfo{
        private Long id;
        @ApiModelProperty(value = "이메일", example = "bookand@example.com")
        private String email;
        @ApiModelProperty(value = "소셜 이메일 정보", example = "bookand@example.com")
        private String providerEmail;
        @ApiModelProperty(value = "닉네임", example = "bookand")
        private String nickname;

        public static MemberInfo of(Account account) {
           return MemberInfo.builder()
                    .id(account.getId())
                    .email(account.getEmail())
                    .providerEmail(account.getProviderEmail())
                    .nickname(account.getNickname())
                    .build();
        }
    }

    public record ManagerInfo(
            Long id,
            String email,
            String password,
            String nickname
    ) {}

    public record IsAvailableNickname(
            boolean isAvailable
    ) {}
}