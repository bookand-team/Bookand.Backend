package kr.co.bookand.backend.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.account.domain.dto.TokenDto;
import kr.co.bookand.backend.account.service.AuthService;

import kr.co.bookand.backend.common.domain.Message;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        LoginResponse loginResponse = authService.socialAccess(authRequest);
        return ResponseEntity.status(loginResponse.getHttpStatus()).body(loginResponse.getTokenResponse());
    }

    @ApiOperation(value = "회원 가입")
    @PostMapping("/sign")
    public ResponseEntity<TokenResponse> sign(@RequestBody SignDto signDto) {
        TokenResponse tokenResponse = authService.socialSignUp(signDto);
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }

    @ApiOperation(value = "토큰 재발행")
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody TokenRequest tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @ApiOperation(value = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<Message> logout() {
        return ResponseEntity.ok(authService.logout());
    }

    @ApiOperation(value = "어드민 전용")
    @PostMapping("/admin")
    public ResponseEntity<TokenResponse> loginAdmin(@RequestBody LoginRequest loginRequestDto) {
        return ResponseEntity.ok(authService.adminLogin(loginRequestDto));
    }

    @ApiOperation(value = "매니저 전용")
    @PostMapping("/manager")
    public ResponseEntity<TokenResponse> loginManager(@RequestBody LoginRequest loginRequestDto) {
        return ResponseEntity.ok(authService.managerLogin(loginRequestDto));
    }

    @ApiOperation(value = "매니저 생성")
    @PostMapping("/admin/manager")
    public ResponseEntity<Message> createManager(@RequestBody ManagerInfo memberInfoDto) {
        return ResponseEntity.ok(authService.createManager(memberInfoDto));
    }
}
