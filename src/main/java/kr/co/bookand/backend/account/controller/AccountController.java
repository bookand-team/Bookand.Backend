package kr.co.bookand.backend.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.dto.AccountDto;
import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.account.util.AccountUtil;
import kr.co.bookand.backend.common.ApiResponse;
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
@Api(tags = "회원 API", hidden = true)
public class AccountController {

    private final AccountService accountService;

    @ApiOperation(value = "회원 정보")
    @GetMapping("/me")
    public ApiResponse<MemberInfo> getMyAccountInfo() {
        return ApiResponse.success(accountService.getAccount());
    }

    @ApiOperation(value = "회원 프로필 변경")
    @PostMapping("/profile")
    public ApiResponse<MemberInfo> updateNickname(@Valid @RequestBody AccountDto.MemberUpdateRequest memberRequestUpdateDto) {
        return ApiResponse.success(accountService.updateNickname(memberRequestUpdateDto));
    }

    @ApiOperation(value = "닉네임 검증 API")
    @GetMapping("/nickname/{nickname}")
    public ApiResponse<Boolean> validNickname(@PathVariable String nickname) {
        return ApiResponse.success(accountService.validNickname(nickname));
    }

    @ApiOperation(value = "회원 삭제(개선할 예정)")
    @DeleteMapping("/remove")
    public Message removeAccount() {
        Account account = AccountUtil.getAccount();
        accountService.removeAccount(account);
        return Message.of("유저 삭제 완료");
    }
}
