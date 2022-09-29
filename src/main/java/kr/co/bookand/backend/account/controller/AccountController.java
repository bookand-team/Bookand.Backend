package kr.co.bookand.backend.account.controller;

import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.common.CodeStatus;
import kr.co.bookand.backend.common.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public Message updateNickname(@RequestBody String nickname) {
        Message message = accountService.updateNickname(nickname);
        return message;
    }
}
