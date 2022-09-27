package kr.co.bookand.backend.account.controller;

import kr.co.bookand.backend.account.domain.dto.AuthDto;
import kr.co.bookand.backend.account.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthDto.AuthResponseMessage> login(@RequestBody AuthDto.AuthRequest authRequest) {
        return new ResponseEntity<>(authService.socialAccess(authRequest), HttpStatus.OK);
    }
}
