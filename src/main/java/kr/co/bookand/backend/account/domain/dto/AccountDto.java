package kr.co.bookand.backend.account.domain.dto;


import io.swagger.annotations.ApiModelProperty;
import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.Role;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
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


    public record MemberUpdateRequest (
        String profileImage,
        String nickname
    ) {}


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
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }


    public record MemberInfo(
            Long id,
            String email,
            String providerEmail,
            String nickname,
            String profileImage,
            String providerType
    ) {
        @Builder
        public MemberInfo {
        }

        public static MemberInfo of(Account account) {
            return MemberInfo.builder()
                    .id(account.getId())
                    .email(account.getEmail())
                    .providerEmail(account.getProviderEmail())
                    .nickname(account.getNickname())
                    .profileImage(account.getProfileImage())
                    .providerType(account.getProvider())
                    .build();
        }
    }

    public record ManagerInfo(
            Long id,
            String email,
            String password,
            String nickname
    ) {
    }

    public record NicknameResponse(
            String nickname
    ) {
        public static NicknameResponse of(String nicknameRandom) {
            return new NicknameResponse(nicknameRandom);
        }
    }

    public record MemberListResponse(
            PageResponse<MemberInfo> memberList

    ) {
        @Builder
        public MemberListResponse {
        }

        public static MemberListResponse of(Page<MemberInfo> memberList) {
            return MemberListResponse.builder()
                    .memberList(PageResponse.of(memberList))
                    .build();
        }
    }
}
