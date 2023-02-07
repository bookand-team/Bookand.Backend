package kr.co.bookand.backend.policy.service;

import kr.co.bookand.backend.policy.domain.dto.PolicyDto;

public class PolicyMockUtils {

    public static PolicyDto createPolicyRequest() {
        return PolicyDto.builder()
                .title("title")
                .context("context")
                .build();
    }
}
