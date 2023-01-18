package kr.co.bookand.backend.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("api/v1/account")
@RequiredArgsConstructor
@Api(tags = "회원 API", hidden = true)
public class AccountController {

    private final AccountService accountService;

    @ApiOperation(value = "회원 정보")
    @GetMapping("/me")
    public ResponseEntity<MemberInfo> getMyAccountInfo() {
        return ResponseEntity.ok(accountService.getAccount());
    }

    @ApiOperation(value = "회원 프로필 변경")
    @PostMapping("/profile")
    public ResponseEntity<MemberInfo> updateNickname(@Valid @RequestBody AccountDto.MemberUpdateRequest memberRequestUpdateDto) {
        return ResponseEntity.ok(accountService.updateNickname(memberRequestUpdateDto));
    }

    @ApiOperation(value = "닉네임 검증 API")
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<Boolean> validNickname(@PathVariable String nickname) {
        return ResponseEntity.ok(accountService.validNickname(nickname));
    }

    @ApiOperation(value = "회원 삭제(개선할 예정)")
    @DeleteMapping("/remove")
    public ResponseEntity<Message> removeAccount() {
        Account account = AccountUtil.getAccount();
        accountService.removeAccount(account);
        return ResponseEntity.ok(Message.of("유저 삭제 완료"));
    }
}
