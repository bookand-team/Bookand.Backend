package kr.co.bookand.backend.policy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.account.domain.dto.AccountDto;
import kr.co.bookand.backend.common.ApiResponse;
import kr.co.bookand.backend.common.Message;
import kr.co.bookand.backend.policy.domain.dto.PolicyDto;
import kr.co.bookand.backend.policy.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/policy")
@RequiredArgsConstructor
@Api(tags = "정책 API")
public class PolicyController {

    private final PolicyService policyService;

    @ApiOperation(value = "정책 조회")
    @GetMapping("/{id}")
    public ResponseEntity<PolicyDto> getPolicy(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.getPolicy(id));
    }

    @ApiOperation(value = "정책 수정")
    @PutMapping()
    public ResponseEntity<PolicyDto> updatePolicy(@RequestBody PolicyDto policyDto) {
        return ResponseEntity.ok(policyService.updatePolicy(policyDto));
    }

    @ApiOperation(value = "정책 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Message> deletePolicy(@PathVariable Long id) {
        policyService.removePolicy(id);
        return ResponseEntity.ok(Message.of("삭제 완료"));
    }

    @ApiOperation(value = "정책 생성")
    @PostMapping()
    public ResponseEntity<PolicyDto> createPolicy(@RequestBody PolicyDto policyDto) {
        return ResponseEntity.ok(policyService.createPolicy(policyDto));
    }

}
