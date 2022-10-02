package kr.co.bookand.backend.account.controller;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.account.util.AccountUtil;
import kr.co.bookand.backend.common.Message;
import lombok.RequiredArgsConstructor;
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
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/me")
    public MemberInfo getMyAccountInfo() {
        return accountService.getAccount();
    }

    @PostMapping("/nickname")
    public MemberInfo updateNickname(@Valid @RequestBody MemberRequestUpdate memberRequestUpdateDto) {
        return accountService.updateNickname(memberRequestUpdateDto);
    }

    @DeleteMapping("/remove")
    public Message removeAccount() {
        Account account = AccountUtil.getAccount();
        accountService.removeAccount(account);
        return Message.of("유저 삭제 완료");
    }
}
