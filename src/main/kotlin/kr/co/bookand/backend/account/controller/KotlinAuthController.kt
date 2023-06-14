package kr.co.bookand.backend.account.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.co.bookand.backend.account.domain.dto.KotlinAuthRequest
import kr.co.bookand.backend.account.domain.dto.KotlinLoginRequest
import kr.co.bookand.backend.account.domain.dto.KotlinManagerInfoRequest
import kr.co.bookand.backend.account.service.KotlinAuthService
import kr.co.bookand.backend.common.domain.KotlinMessageResponse
import kr.co.bookand.backend.config.jwt.SignTokenRequest
import kr.co.bookand.backend.config.jwt.TokenRequest
import kr.co.bookand.backend.config.jwt.TokenResponse
import kr.co.bookand.backend.config.security.KotlinSecurityUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/auth")
@Api(tags = ["로그인 API"])
class KotlinAuthController(
    private val authService: KotlinAuthService
) {
    @ApiOperation(value = "소셜 로그인")
    @PostMapping("/login")
    fun login(
        @RequestBody authRequest: KotlinAuthRequest
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
    fun logout(): ResponseEntity<KotlinMessageResponse> {
        return ResponseEntity.ok(authService.logout())
    }

    @ApiOperation(value = "어드민 전용")
    @PostMapping("/admin")
    fun loginAdmin(
        @RequestBody loginRequest: KotlinLoginRequest
    ): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(authService.adminLogin(loginRequest))
    }

    @ApiOperation(value = "매니저 전용")
    @PostMapping("/manager")
    fun loginManager(
        @RequestBody loginRequest: KotlinLoginRequest
    ): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(authService.managerLogin(loginRequest))
    }

    @ApiOperation(value = "매니저 생성")
    @PostMapping("/admin/manager")
    fun createManager(
        @RequestBody kotlinManagerInfoRequest: KotlinManagerInfoRequest
    ): ResponseEntity<KotlinMessageResponse> {
        val account = authService.getAccountByEmail(KotlinSecurityUtils.getCurrentAccountEmail())
        return ResponseEntity.ok(authService.createManager(account, kotlinManagerInfoRequest))
    }
}