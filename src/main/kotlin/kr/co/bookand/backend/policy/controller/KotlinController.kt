package kr.co.bookand.backend.policy.controller

import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.Operation
import kr.co.bookand.backend.policy.domain.dto.KotlinPolicyResponse
import kr.co.bookand.backend.policy.domain.dto.PolicyDto.PolicyResponse
import kr.co.bookand.backend.policy.service.KotlinPolicyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class KotlinController(
    private val kotlinPolicyService: KotlinPolicyService
) {

    @ApiOperation(value = "정책 조회")
    @Operation(
        summary = "정책 조회", description = """정책 조회 입니다. 조회 목록으로는 이용약관 - /terms
        개인정보 수집 및 이용 동의 - /personal-info-agree
        위치기반 서비스 이용약관 - /location-base-terms
        개인정보처리방침 - /privacy
        운영정책 - /operation 이 있습니다."""
    )
    @GetMapping("/{policyName}")
    fun getPolicy(
        @PathVariable policyName: String
    ): ResponseEntity<KotlinPolicyResponse>? {
        return ResponseEntity.ok(kotlinPolicyService.getTitlePolicy(policyName))
    }
}