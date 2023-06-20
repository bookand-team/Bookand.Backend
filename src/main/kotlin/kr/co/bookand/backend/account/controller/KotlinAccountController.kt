package kr.co.bookand.backend.account.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.Operation
import kr.co.bookand.backend.account.domain.dto.*
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.common.domain.KotlinMessageResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/members")
@Api(tags = ["회원 API"], hidden = true)
class KotlinAccountController(
    private val kotlinAccountService: KotlinAccountService,
) {

    @ApiOperation(value = "회원 정보")
    @Operation(summary = "회원 정보", description = "헤더 값을 넣어서 자신의 회원 정보를 받아옵니다.")
    @GetMapping("/me")
    fun getMyAccountInfo(): ResponseEntity<KotlinAccountInfoResponse> {
        return ResponseEntity.ok(kotlinAccountService.getMyAccountInfo())
    }

    @ApiOperation(value = "회원 조회 (id)")
    @Operation(summary = "회원 조회", description = "회원의 정보를 조회합니다.")
    @GetMapping("/{id}")
    fun getAccountInfo(
        @PathVariable id: Long
    ): ResponseEntity<KotlinAccountInfoResponse> {
        return ResponseEntity.ok(kotlinAccountService.getAccountInfoById(id))
    }

    @ApiOperation(value = "회원 조회 (닉네임)")
    @Operation(summary = "회원 조회", description = "회원의 정보를 조회합니다.")
    @GetMapping("/nickname/{nickname}")
    fun getAccountInfo(
        @PathVariable nickname: String
    ): ResponseEntity<KotlinAccountInfoResponse> {
        return ResponseEntity.ok(kotlinAccountService.getAccountByNickname(nickname))
    }

    @ApiOperation(value = "회원 프로필 변경")
    @Operation(summary = "회원 프로필 변경", description = "프로필 정보를 넣어서 변경합니다")
    @PutMapping("/profile")
    fun updateNickname(
        @Valid @RequestBody kotlinAccountRequest: KotlinAccountRequest
    ): ResponseEntity<KotlinAccountInfoResponse> {
        val account = kotlinAccountService.getCurrentAccount()
        return ResponseEntity.ok(kotlinAccountService.updateAccount(account, kotlinAccountRequest))
    }

    @ApiOperation(value = "닉네임 검증")
    @Operation(summary = "닉네임 검증", description = "닉네임을 검증합니다. 중복되면 true, 중복되지 않으면 false 를 반환합니다.")
    @GetMapping("/nickname/{nickname}/check")
    fun validNickname(
        @PathVariable nickname: String
    ): ResponseEntity<KotlinMessageResponse> {
        val message: KotlinMessageResponse = kotlinAccountService.existNickname(nickname)
        return ResponseEntity.status(message.statusCode).body(message)
    }

    @ApiOperation(value = "회원 탈퇴 사유 입력")
    @Operation(
        summary = "회원 탈퇴 사유 입력", description = """회원 탈퇴 사유를 입력합니다.탈퇴 사유 종류는     NOT_ENOUGH_CONTENT("콘텐츠가 만족스럽지 않아요"),
    UNCOMFORTABLE("이용 방법이 불편해요"),
    PRIVACY("개인정보 보안이 걱정돼요"),
    ETC("기타") 입니다.탈퇴 reasone 은 필수는 아니고socialAccessToken 에 accessToken 값을 입력해야 됩니다"""
    )
    @DeleteMapping("/revoke")
    fun revokeReason(
        @RequestBody @Valid revokeReasonRequest :KotlinRevokeReasonRequest
    ): ResponseEntity<KotlinMessageResponse> {
        val account = kotlinAccountService.getCurrentAccount()
        val revokeAccount: Boolean = kotlinAccountService.revokeAccount(account, revokeReasonRequest)
        return ResponseEntity.ok(KotlinMessageResponse(message = revokeAccount.toString(), statusCode = 200))
    }

    @ApiOperation(value = "닉네임 랜덤 생성")
    @Operation(summary = "닉네임 랜덤 생성", description = "닉네임을 랜덤으로 생성합니다.")
    @GetMapping("/nickname/random")
    fun randomNickname(): ResponseEntity<KotlinNicknameResponse> {
        return ResponseEntity.ok(kotlinAccountService.getRandomNickname())
    }

    @ApiOperation(value = "회원 전체 조회 (관리자)")
    @Operation(summary = "회원 전체 조회", description = "회원 전체를 조회합니다.")
    @GetMapping("/list")
    fun getAccountList(
        @PageableDefault pageable: Pageable
    ): ResponseEntity<KotlinAccountListResponse>? {
        return ResponseEntity.ok(kotlinAccountService.getAccountList(pageable))
    }

    @ApiOperation(value = "회원 정지 (관리자)")
    @Operation(
        summary = "회원 정지 (관리자)", description = "회원을 정지합니다." +
                "1회 정지는 SUSPENDED 상태에 7일 정지, 2회 정지는 DELETED 상태에 6개월 정지"
    )
    @PutMapping("/suspend/{id}")
    fun suspendAccount(
        @PathVariable id: Long
    ): ResponseEntity<KotlinMessageResponse> {
        val account = kotlinAccountService.getCurrentAccount()
        val suspendedAccount = kotlinAccountService.suspendedAccount(account, id)
        return ResponseEntity.ok(KotlinMessageResponse(message = suspendedAccount.name, statusCode = 200))
    }

}