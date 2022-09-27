package kr.co.bookand.backend.account.domain.dto;


import kr.co.bookand.backend.account.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AccountDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MemberRequest {
        private String email;
        private String password;
        private String nickname;
        private String socialType;

        public Account toAccount(PasswordEncoder passwordEncoder) {
            return Account.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .nickname(nickname)
                    .provider(socialType)
                    .build();
        }

        public LoginRequest toLoginRequest() {
            return LoginRequest.builder()
                    .email(email)
                    .password(password)
                    .build();
        }

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email,password);
        }

    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class LoginRequest {
        private String email;
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email,password);
        }
    }

}