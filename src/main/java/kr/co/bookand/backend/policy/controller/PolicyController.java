package kr.co.bookand.backend.policy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "정책 API")
public class PolicyController {

    private final PolicyService policyService;

    @ApiOperation(value = "정책 조회")
    @GetMapping("/{id}")
    public ApiResponse<PolicyDto> getPolicy(@PathVariable Long id) {
        return ApiResponse.success(policyService.getPolicy(id));
    }

    @ApiOperation(value = "정책 수정")
    @PutMapping()
    public ApiResponse<PolicyDto> updatePolicy(@RequestBody PolicyDto policyDto) {
        return ApiResponse.success(policyService.updatePolicy(policyDto));
    }

    @ApiOperation(value = "정책 삭제")
    @DeleteMapping("/{id}")
    public Message deletePolicy(@PathVariable Long id) {
        policyService.removePolicy(id);
        return Message.of("삭제 완료");
    }

    @ApiOperation(value = "정책 생성")
    @PostMapping()
    public ApiResponse<PolicyDto> createPolicy(@RequestBody PolicyDto policyDto) {
        return ApiResponse.success(policyService.createPolicy(policyDto));
    }

}
