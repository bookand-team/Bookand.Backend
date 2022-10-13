package kr.co.bookand.backend.account.controller;

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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@RequestBody AuthRequest authRequest) {
        return ApiResponse.success(authService.socialAccess(authRequest));
    }

    @PostMapping("/reissue")
    public ApiResponse<TokenResponse> reissue(@RequestBody TokenRequest tokenRequestDto) {
        return ApiResponse.success(authService.reissue(tokenRequestDto));
    }

    @GetMapping("/logout")
    public Message logout() {
        return Message.of(authService.logout().getResult());
    }

    @PostMapping("/admin")
    public ApiResponse<TokenResponse> loginAdmin(@RequestBody LoginRequest loginRequestDto) {
        return ApiResponse.success(authService.adminLogin(loginRequestDto));
    }
}
