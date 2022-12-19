package kr.co.bookand.backend.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.account.service.AuthService;

import kr.co.bookand.backend.common.ApiResponse;
import kr.co.bookand.backend.common.Message;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import static kr.co.bookand.backend.account.domain.dto.AccountDto.*;
import static kr.co.bookand.backend.account.domain.dto.AuthDto.*;
import static kr.co.bookand.backend.account.domain.dto.TokenDto.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Api(tags = "로그인 API")
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "소셜 로그인")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody AuthRequest authRequest) {
        return ApiResponse.success(authService.socialAccess(authRequest));
    }

    @ApiOperation(value = "토큰 재발행")
    @PostMapping("/reissue")
    public ApiResponse<TokenResponse> reissue(@RequestBody TokenRequest tokenRequestDto) {
        return ApiResponse.success(authService.reissue(tokenRequestDto));
    }

    @ApiOperation(value = "로그아웃")
    @GetMapping("/logout")
    public Message logout() {
        return Message.of(authService.logout().getResult());
    }

    @ApiOperation(value = "어드민 전용")
    @PostMapping("/admin")
    public ApiResponse<TokenResponse> loginAdmin(@RequestBody LoginRequest loginRequestDto) {
        return ApiResponse.success(authService.adminLogin(loginRequestDto));
    }
}
