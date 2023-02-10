package kr.co.bookand.backend.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.dto.AccountDto;
import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.account.util.AccountUtil;
import kr.co.bookand.backend.common.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static kr.co.bookand.backend.account.domain.dto.AccountDto.*;

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
    @Operation(summary = "회원 프로필 변경", description = "닉네임 정보를 넣어서 회원 정보를 변경합니다")
    @PutMapping("/profile")
    public ResponseEntity<MemberInfo> updateNickname(@Valid @RequestBody MemberUpdateRequest memberRequestUpdateDto) {
        return ResponseEntity.ok(accountService.updateNickname(memberRequestUpdateDto));
    }

    @ApiOperation(value = "닉네임 검증")
    @Operation(summary = "닉네임 검증", description = "닉네임을 검증합니다. 중복되면 true, 중복되지 않으면 false 를 반환합니다.")
    @GetMapping("/nickname/{nickname}/check")
    public ResponseEntity<IsAvailableNickname> validNickname(@PathVariable String nickname) {
        return ResponseEntity.ok(accountService.validNickname(nickname));
    }

    @ApiOperation(value = "회원 삭제")
    @Operation(summary = "회원 삭제", description = "회원을 삭제합니다.")
    @DeleteMapping("/remove")
    public ResponseEntity<Message> removeAccount() {
        Account account = AccountUtil.getAccount();
        accountService.removeAccount(account);
        return ResponseEntity.ok(Message.of("유저 삭제 완료"));
    }
}
