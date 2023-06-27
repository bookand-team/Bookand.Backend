package kr.co.bookand.backend.account.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.Operation
import kr.co.bookand.backend.account.dto.*
import kr.co.bookand.backend.account.model.AccountStatus
import kr.co.bookand.backend.account.model.Role
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.common.model.MessageResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/members")
@Api(tags = ["회원 API"], hidden = true)
class AccountController(
    private val accountService: AccountService,
) {

    @ApiOperation(value = "회원 정보")
    @Operation(summary = "회원 정보", description = "헤더 값을 넣어서 자신의 회원 정보를 받아옵니다.")
    @GetMapping("/me")
    fun getMyAccountInfo(): ResponseEntity<AccountInfoResponse> {
        return ResponseEntity.ok(accountService.getMyAccountInfo())
    }

    @ApiOperation(value = "회원 조회 (id) (관리자)")
    @Operation(summary = "회원 조회", description = "회원의 정보를 조회합니다.")
    @GetMapping("/{id}")
    fun getAccountInfo(
        @PathVariable id: Long
    ): ResponseEntity<AccountDetailInfoResponse> {
        return ResponseEntity.ok(accountService.getAccountDetailInfoById(id))
    }

    @ApiOperation(value = "회원 조회 (닉네임) (관리자)")
    @Operation(summary = "회원 조회", description = "회원의 정보를 조회합니다.")
    @GetMapping("/nickname/{nickname}")
    fun getAccountInfo(
        @PathVariable nickname: String
    ): ResponseEntity<AccountDetailInfoResponse> {
        return ResponseEntity.ok(accountService.getAccountByNickname(nickname))
    }

    @ApiOperation(value = "회원 프로필 변경")
    @Operation(summary = "회원 프로필 변경", description = "프로필 정보를 넣어서 변경합니다")
    @PutMapping("/profile")
    fun updateNickname(
        @Valid @RequestBody accountRequest: AccountRequest
    ): ResponseEntity<AccountIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(accountService.updateAccount(account, accountRequest))
    }

    @ApiOperation(value = "닉네임 검증")
    @Operation(summary = "닉네임 검증", description = "닉네임을 검증합니다. 중복되면 true, 중복되지 않으면 false 를 반환합니다.")
    @GetMapping("/nickname/{nickname}/check")
    fun validNickname(
        @PathVariable nickname: String
    ): ResponseEntity<MessageResponse> {
        val message: MessageResponse = accountService.existNickname(nickname)
        return ResponseEntity.status(message.statusCode).body(message)
    }

    @ApiOperation(value = "회원 탈퇴 사유 입력")
    @Operation(
        summary = "회원 탈퇴 사유 입력",
        description = """회원 탈퇴 사유를 입력합니다.탈퇴 사유 종류는     
            NOT_ENOUGH_CONTENT("콘텐츠가 만족스럽지 않아요"),
            UNCOMFORTABLE("이용 방법이 불편해요"),
            PRIVACY("개인정보 보안이 걱정돼요"),
            ETC("기타") 입니다.
            탈퇴 reasone 은 필수는 아니고socialAccessToken 에 accessToken 값을 입력해야 됩니다"""
    )
    @DeleteMapping("/revoke")
    fun revokeReason(
        @RequestBody @Valid revokeReasonRequest: RevokeReasonRequest
    ): ResponseEntity<MessageResponse> {
        val account = accountService.getCurrentAccount()
        val revokeAccount: Boolean = accountService.revokeAccount(account, revokeReasonRequest)
        return ResponseEntity.ok(MessageResponse(result = revokeAccount.toString(), statusCode = 200))
    }

    @ApiOperation(value = "닉네임 랜덤 생성")
    @Operation(summary = "닉네임 랜덤 생성", description = "닉네임을 랜덤으로 생성합니다.")
    @GetMapping("/nickname/random")
    fun randomNickname(): ResponseEntity<NicknameResponse> {
        return ResponseEntity.ok(accountService.getRandomNickname())
    }

    @ApiOperation(value = "회원 전체 조회 (관리자)")
    @Operation(summary = "회원 전체 조회", description = "회원 전체를 조회합니다.")
    @GetMapping("/list")
    fun getAccountList(
        @PageableDefault pageable: Pageable
    ): ResponseEntity<AccountListResponse>? {
        return ResponseEntity.ok(accountService.getAccountList(pageable))
    }

    @ApiOperation(value = "회원 상태 변경 (관리자)")
    @Operation(
        summary = "회원 상태 변경 (관리자)",
        description = "회원을 상태를 변경합니다." +
                "맴버 타입은 USER, ADMIN 이 있습니다." +
                "이용 상태는 NORMAL, DORMANCY ,SUSPENDED, DELETED 가 있습니다."
    )
    @PutMapping("/{id}/status")
    fun suspendAccount(
        @PathVariable id: Long,
        @RequestBody @Valid accountStatusRequest: AccountStatusRequest
    ): ResponseEntity<AccountIdResponse> {
        val account = accountService.getCurrentAccount()
        val changeAccount = accountService.changeAccountStatus(account, id, accountStatusRequest)
        return ResponseEntity.ok(changeAccount)
    }

    @ApiOperation(value = "회원 탈퇴 (관리자)")
    @Operation(summary = "회원 탈퇴 (관리자)", description = "회원을 탈퇴 시킵니다.")
    @DeleteMapping("/{id}")
    fun deleteAccount(
        @PathVariable id: Long
    ): ResponseEntity<MessageResponse> {
        val account = accountService.getCurrentAccount()
        val deleteAccount = accountService.deleteAccount(account, id)
        return ResponseEntity.ok(MessageResponse(result = deleteAccount.toString(), statusCode = 200))
    }

    @ApiOperation(value = "회원 필터 조회 (관리자)")
    @Operation(summary = "회원 필터 조회 (관리자)", description = "회원 필터 조회 (관리자)")
    @GetMapping("/filter")
    fun getAccountFilterList(
        @PageableDefault pageable: Pageable,
        @RequestParam("account-status") accountStatus: AccountStatus?,
        @RequestParam("role") role: Role?,
    ): ResponseEntity<AccountListResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(accountService.getAccountFilterList(account, pageable, accountStatus, role))
    }
}