package kr.co.bookand.backend.policy.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.Operation
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.common.domain.KotlinMessageResponse
import kr.co.bookand.backend.policy.domain.dto.KotlinPolicyIdResponse
import kr.co.bookand.backend.policy.domain.dto.KotlinPolicyRequest
import kr.co.bookand.backend.policy.domain.dto.KotlinPolicyResponse
import kr.co.bookand.backend.policy.service.KotlinPolicyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/policys")
@Api(tags = ["정책 API"])
class KotlinController(
    private val policyService: KotlinPolicyService,
    private val accountService: KotlinAccountService
) {

    @ApiOperation(value = "정책 생성")
    @PostMapping
    fun createPolicy(
        @RequestBody policyRequest: KotlinPolicyRequest
    ): ResponseEntity<KotlinPolicyIdResponse> {
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
    ): ResponseEntity<KotlinPolicyResponse> {
        return ResponseEntity.ok(policyService.getTitlePolicy(policyName))
    }

    @ApiOperation(value = "정책 수정")
    @PutMapping("/{id}")
    fun updatePolicy(
        @RequestBody policyRequest: KotlinPolicyRequest,
        @PathVariable id: Long
    ): ResponseEntity<KotlinPolicyIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(policyService.updatePolicy(account, id, policyRequest))
    }

    @ApiOperation(value = "정책 삭제")
    @DeleteMapping("/{id}")
    fun deletePolicy(
        @PathVariable id: Long
    ): ResponseEntity<KotlinMessageResponse> {
        val account = accountService.getCurrentAccount()
        policyService.removePolicy(account, id)
        return ResponseEntity.ok(KotlinMessageResponse(message = "정책이 삭제되었습니다.", statusCode = 200))
    }

}