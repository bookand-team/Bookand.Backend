package kr.co.bookand.backend.account.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.co.bookand.backend.account.domain.dto.AuthRequest
import kr.co.bookand.backend.account.domain.dto.LoginRequest
import kr.co.bookand.backend.account.domain.dto.ManagerInfoRequest
import kr.co.bookand.backend.account.service.AuthService
import kr.co.bookand.backend.common.domain.MessageResponse
import kr.co.bookand.backend.account.domain.dto.SignTokenRequest
import kr.co.bookand.backend.account.domain.dto.TokenRequest
import kr.co.bookand.backend.account.domain.dto.TokenResponse
import kr.co.bookand.backend.config.security.SecurityUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = ["로그인 API"])
class AuthController(
    private val authService: AuthService
) {
    @ApiOperation(value = "소셜 로그인")
    @PostMapping("/login")
    fun login(
        @RequestBody authRequest: AuthRequest
    ): ResponseEntity<*> {
        val loginResponse = authService.socialAccess(authRequest)
        return ResponseEntity
            .status(loginResponse.httpStatus)
            .body(loginResponse.tokenResponse)
    }

    @ApiOperation(value = "회원 가입")
    @PostMapping("/signup")
    fun signUp(
        @RequestBody signTokenRequest: SignTokenRequest
    ): ResponseEntity<TokenResponse>{
        val tokenResponse: TokenResponse = authService.socialSignUp(signTokenRequest)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(tokenResponse)
    }

    @ApiOperation(value = "토큰 재발행")
    @PostMapping("/reissue")
    fun reissue(
        @RequestBody tokenRequest: TokenRequest
    ): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(authService.reissue(tokenRequest))
    }

    @ApiOperation(value = "로그아웃")
    @GetMapping("/logout")
    fun logout(): ResponseEntity<MessageResponse> {
        return ResponseEntity.ok(authService.logout())
    }

    @ApiOperation(value = "어드민 전용")
    @PostMapping("/admin")
    fun loginAdmin(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(authService.adminLogin(loginRequest))
    }

    @ApiOperation(value = "매니저 전용")
    @PostMapping("/manager")
    fun loginManager(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(authService.managerLogin(loginRequest))
    }

    @ApiOperation(value = "매니저 생성")
    @PostMapping("/admin/manager")
    fun createManager(
        @RequestBody managerInfoRequest: ManagerInfoRequest
    ): ResponseEntity<MessageResponse> {
        val account = authService.getAccountByEmail(SecurityUtils.getCurrentAccountEmail())
        return ResponseEntity.ok(authService.createManager(account, managerInfoRequest))
    }
}