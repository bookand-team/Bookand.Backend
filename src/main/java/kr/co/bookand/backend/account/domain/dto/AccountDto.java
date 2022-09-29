package kr.co.bookand.backend.account.domain.dto;


import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.common.CodeStatus;
import kr.co.bookand.backend.common.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AccountDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MemberRequest {
        private String email;
        private String password;
        private String nickname;
        private String socialType;

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

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MemberInfo{
        private String email;
        private String nickname;

        public static MemberInfo of(Account account) {
            return new MemberInfo(account.getEmail(), account.getNickname());
        }
    }


    @Getter
    @AllArgsConstructor
    @Builder
    public static class MemberResponseMessage {
        private Message message;
        private MemberInfo data;

        public static MemberResponseMessage of(CodeStatus status, String message, MemberInfo data){
            return MemberResponseMessage.builder()
                    .data(data)
                    .message(
                        Message.builder().
                                status(status).
                                msg(message).
                                build()
                    )
                    .build();
        }
    }

}