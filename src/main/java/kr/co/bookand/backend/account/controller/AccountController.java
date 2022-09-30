package kr.co.bookand.backend.account.controller;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.account.util.AccountUtil;
import kr.co.bookand.backend.common.CodeStatus;
import kr.co.bookand.backend.common.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static kr.co.bookand.backend.account.domain.dto.AccountDto.*;

@RestController
@RequestMapping("api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/me")
    public MemberResponseMessage getMyAccountInfo() {
        MemberInfo data = accountService.getAccount();
        return MemberResponseMessage.of(CodeStatus.SUCCESS, "조회 완료", data);
    }

    @PostMapping("/nickname")
    public MemberResponseMessage updateNickname(@Valid @RequestBody MemberRequestUpdate memberRequestUpdateDto) {
        MemberInfo data = accountService.updateNickname(memberRequestUpdateDto);
        return MemberResponseMessage.of(CodeStatus.SUCCESS, "변경 완료", data);
    }

    @DeleteMapping("/remove")
    public MemberResponseMessage removeAccount() {
        Account account = AccountUtil.getAccount();
        accountService.removeAccount(account);
        return MemberResponseMessage.of(CodeStatus.SUCCESS, "유저 삭제", null);
    }
}
