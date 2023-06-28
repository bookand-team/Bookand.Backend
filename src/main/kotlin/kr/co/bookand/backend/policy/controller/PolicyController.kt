package kr.co.bookand.backend.policy.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.Operation
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.common.model.MessageResponse
import kr.co.bookand.backend.policy.dto.PolicyIdResponse
import kr.co.bookand.backend.policy.dto.CreatePolicyRequest
import kr.co.bookand.backend.policy.dto.PolicyResponse
import kr.co.bookand.backend.policy.service.PolicyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/policys")
@Api(tags = ["정책 API"])
class PolicyController(
    private val policyService: PolicyService,
    private val accountService: AccountService
) {

    @ApiOperation(value = "정책 생성 (WEB)")
    @PostMapping
    fun createPolicy(
        @RequestBody policyRequest: CreatePolicyRequest
    ): ResponseEntity<PolicyIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(policyService.createPolicy(account, policyRequest))
    }

    @ApiOperation(value = "정책 조회")
    @Operation(
        summary = "정책 조회",
        description = """정책 조회 입니다. 조회 목록으로는 이용약관 - /terms
        개인정보 수집 및 이용 동의 - /personal-info-agree
        위치기반 서비스 이용약관 - /location-base-terms
        개인정보처리방침 - /privacy
        운영정책 - /operation 이 있습니다."""
    )
    @GetMapping("/{policyName}")
    fun getPolicy(
        @PathVariable policyName: String
    ): ResponseEntity<PolicyResponse> {
        return ResponseEntity.ok(policyService.getTitlePolicy(policyName))
    }

    @ApiOperation(value = "정책 수정 (WEB)")
    @PutMapping("/{id}")
    fun updatePolicy(
        @RequestBody policyRequest: CreatePolicyRequest,
        @PathVariable id: Long
    ): ResponseEntity<PolicyIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(policyService.updatePolicy(account, id, policyRequest))
    }

    @ApiOperation(value = "정책 삭제 (WEB)")
    @DeleteMapping("/{id}")
    fun deletePolicy(
        @PathVariable id: Long
    ): ResponseEntity<MessageResponse> {
        val account = accountService.getCurrentAccount()
        policyService.removePolicy(account, id)
        return ResponseEntity.ok(MessageResponse(result = "정책이 삭제되었습니다.", statusCode = 200))
    }

}