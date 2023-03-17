package kr.co.bookand.backend.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.AccountStatus;
import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.account.util.AccountUtil;
import kr.co.bookand.backend.common.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static kr.co.bookand.backend.account.domain.dto.AccountDto.*;
import static kr.co.bookand.backend.account.domain.dto.RevokeDto.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Api(tags = "회원 API", hidden = true)
public class AccountController {

    private final AccountService accountService;

    @ApiOperation(value = "회원 정보")
    @Operation(summary = "회원 정보", description = "헤더 값을 넣어서 자신의 회원 정보를 받아옵니다.")
    @GetMapping("/me")
    public ResponseEntity<MemberInfo> getMyAccountInfo() {
        return ResponseEntity.ok(accountService.getAccount());
    }

    @ApiOperation(value = "회원 조회 (id)")
    @Operation(summary = "회원 조회", description = "회원의 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<MemberInfo> getAccountInfo(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @ApiOperation(value = "회원 조회 (닉네임)")
    @Operation(summary = "회원 조회", description = "회원의 정보를 조회합니다.")
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<MemberInfo> getAccountInfo(@PathVariable String nickname) {
        return ResponseEntity.ok(accountService.getAccount(nickname));
    }

    @ApiOperation(value = "회원 프로필 변경")
    @Operation(summary = "회원 프로필 변경", description = "프로필 정보를 넣어서 변경합니다")
    @PutMapping("/profile")
    public ResponseEntity<MemberInfo> updateNickname(@Valid @RequestBody MemberUpdateRequest memberRequestUpdateDto) {
        return ResponseEntity.ok(accountService.updateNickname(memberRequestUpdateDto));
    }

    @ApiOperation(value = "닉네임 검증")
    @Operation(summary = "닉네임 검증", description = "닉네임을 검증합니다. 중복되면 true, 중복되지 않으면 false 를 반환합니다.")
    @GetMapping("/nickname/{nickname}/check")
    public ResponseEntity<Message> validNickname(@PathVariable String nickname) {
        Message message = accountService.checkNickname(nickname);
        return ResponseEntity.status(message.statusCode()).body(message);
    }

    @ApiOperation(value = "회원 탈퇴 사유 입력")
    @Operation(summary = "회원 탈퇴 사유 입력", description = "회원 탈퇴 사유를 입력합니다." +
            "탈퇴 사유 종류는 " +
            "    NOT_ENOUGH_CONTENT(\"콘텐츠가 만족스럽지 않아요\"),\n" +
            "    UNCOMFORTABLE(\"이용 방법이 불편해요\"),\n" +
            "    PRIVACY(\"개인정보 보안이 걱정돼요\"),\n" +
            "    ETC(\"기타\") 입니다." +
            "탈퇴 reasone 은 필수는 아니고" +
            "socialAccessToken 에 accessToken 값을 입력해야 됩니다")
    @DeleteMapping("/revoke")
    public ResponseEntity<Message> revokeReason(@Valid @RequestBody RevokeReasonRequest revokeReasonRequest) {
        Account account = AccountUtil.getAccount();
        boolean revokeAccount = accountService.revokeAccount(account, revokeReasonRequest);
        return ResponseEntity.ok(Message.of(String.valueOf(revokeAccount)));
    }

    @ApiOperation(value = "닉네임 랜덤 생성")
    @Operation(summary = "닉네임 랜덤 생성", description = "닉네임을 랜덤으로 생성합니다.")
    @GetMapping("/nickname/random")
    public ResponseEntity<NicknameResponse> randomNickname() {
        return ResponseEntity.ok(accountService.getRandomNickname());
    }

    @ApiOperation(value = "회원 전체 조회 (관리자)")
    @Operation(summary = "회원 전체 조회", description = "회원 전체를 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<MemberListResponse> getAccountList(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(accountService.getAccountList(pageable));
    }

    @ApiOperation(value = "회원 정지 (관리자)")
    @Operation(summary = "회원 정지 (관리자)", description = "회원을 정지합니다." +
            "1회 정지는 SUSPENDED 상태에 7일 정지, 2회 정지는 DELETED 상태에 6개월 정지")
    @PutMapping("/suspend/{id}")
    public ResponseEntity<Message> suspendAccount(@PathVariable Long id) {
        AccountStatus suspendAccount = accountService.suspendAccount(id);
        return ResponseEntity.ok(Message.of(String.valueOf(suspendAccount)));
    }
}
