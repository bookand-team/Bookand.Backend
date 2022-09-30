package kr.co.bookand.backend.account.controller;

import kr.co.bookand.backend.account.domain.dto.AccountDto;
import kr.co.bookand.backend.account.domain.dto.AuthDto;
import kr.co.bookand.backend.account.domain.dto.TokenDto;
import kr.co.bookand.backend.account.service.AuthService;
import kr.co.bookand.backend.common.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthDto.AuthResponseMessage> login(@RequestBody AuthDto.AuthRequest authRequest) {
        return new ResponseEntity<>(authService.socialAccess(authRequest), HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<AuthDto.TokenMessage> reissue(@RequestBody TokenDto.TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @GetMapping("/logout")
    public ResponseEntity<Message> logout() {
        return ResponseEntity.ok(authService.logout());
    }

    @PostMapping("/login/admin")
    public ResponseEntity<AuthDto.TokenMessage> loginAdmin(@RequestBody AccountDto.LoginRequest loginRequestDto) {
        return ResponseEntity.ok(authService.adminLogin(loginRequestDto));
    }
}
