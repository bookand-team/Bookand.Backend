package kr.co.bookand.backend.policy.controller;

import kr.co.bookand.backend.account.domain.dto.AccountDto;
import kr.co.bookand.backend.common.ApiResponse;
import kr.co.bookand.backend.common.Message;
import kr.co.bookand.backend.policy.domain.dto.PolicyDto;
import kr.co.bookand.backend.policy.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/policy")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @GetMapping("/{title}")
    public ApiResponse<PolicyDto> getPolicy(@PathVariable String title) {
        return ApiResponse.success(policyService.getPolicy(title));
    }

    @PutMapping()
    public ApiResponse<PolicyDto> updatePolicy(@RequestBody PolicyDto policyDto) {
        return ApiResponse.success(policyService.updatePolicy(policyDto));
    }

    @DeleteMapping("/{title}")
    public Message deletePolicy(@PathVariable String title) {
        policyService.removePolicy(title);
        return Message.of("삭제 완료");
    }

    @PostMapping()
    public ApiResponse<PolicyDto> createPolicy(@RequestBody PolicyDto policyDto) {
        return ApiResponse.success(policyService.createPolicy(policyDto));
    }

}
