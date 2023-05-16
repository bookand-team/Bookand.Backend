package kr.co.bookand.backend.account.controller

import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.Operation
import kr.co.bookand.backend.account.domain.dto.KotlinAccountInfoResponse
import kr.co.bookand.backend.account.domain.dto.KotlinAccountRequest
import kr.co.bookand.backend.account.service.KotlinAccountService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class KotlinAccountController(
    private val kotlinAccountService: KotlinAccountService
) {
    @ApiOperation(value = "회원 조회 (id)")
    @Operation(summary = "회원 조회", description = "회원의 정보를 조회합니다.")
    @GetMapping("/{id}")
    fun getAccountInfo(
        @PathVariable id: Long
    ): ResponseEntity<KotlinAccountInfoResponse> {
        return ResponseEntity.ok(kotlinAccountService.getAccount(id))
    }

    @ApiOperation(value = "회원 조회 (닉네임)")
    @Operation(summary = "회원 조회", description = "회원의 정보를 조회합니다.")
    @GetMapping("/nickname/{nickname}")
    fun getAccountInfo(
        @PathVariable nickname: String
    ): ResponseEntity<KotlinAccountInfoResponse> {
        return ResponseEntity.ok(kotlinAccountService.getAccount(nickname))
    }

    @ApiOperation(value = "회원 프로필 변경")
    @Operation(summary = "회원 프로필 변경", description = "프로필 정보를 넣어서 변경합니다")
    @PutMapping("/{id}/profile")
    fun updateNickname(
        @PathVariable id: Long,
        @RequestBody kotlinAccountRequest: KotlinAccountRequest
    ): ResponseEntity<KotlinAccountInfoResponse> {
        return ResponseEntity.ok(kotlinAccountService.updateAccount(id, kotlinAccountRequest))
    }

}