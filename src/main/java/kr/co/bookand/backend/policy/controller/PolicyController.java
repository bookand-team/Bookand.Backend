package kr.co.bookand.backend.policy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import kr.co.bookand.backend.common.domain.Message;
import kr.co.bookand.backend.policy.domain.dto.PolicyDto;
import kr.co.bookand.backend.policy.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.bookand.backend.policy.domain.dto.PolicyDto.*;

@RestController
@RequestMapping("/api/v1/policys")
@RequiredArgsConstructor
@Api(tags = "정책 API")
public class PolicyController {

    private final PolicyService policyService;

    @ApiOperation(value = "정책 조회")
    @Operation(summary =
            "정책 조회 입니다. 조회 목록으로는 " +
                    "이용약관 - /terms\n" +
                    "개인정보 수집 및 이용 동의 - /personal-info-agree\n" +
                    "위치기반 서비스 이용약관 - /location-base-terms\n" +
                    "개인정보처리방침 - /privacy\n" +
                    "운영정책 - /operation " +
                    "이 있습니다.")
    @GetMapping("/{policyName}")
    public ResponseEntity<PolicyResponse> getPolicy(
            @PathVariable String policyName
    ) {
        return ResponseEntity.ok(policyService.getTitlePolicy(policyName));
    }

    @ApiOperation(value = "정책 수정")
    @PutMapping("/{id}")
    public ResponseEntity<PolicyResponse> updatePolicy(
            @RequestBody PolicyRequest policyRequest,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(policyService.updatePolicy(id, policyRequest));
    }

    @ApiOperation(value = "정책 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Message> deletePolicy(
            @PathVariable Long id
    ) {
        policyService.removePolicy(id);
        return ResponseEntity.ok(Message.of("삭제 완료"));
    }

    @ApiOperation(value = "정책 생성")
    @PostMapping()
    public ResponseEntity<PolicyResponse> createPolicy(
            @RequestBody PolicyRequest policyRequest
    ) {
        return ResponseEntity.ok(policyService.createPolicy(policyRequest));
    }

}
