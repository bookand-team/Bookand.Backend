package kr.co.bookand.backend.policy.service;

import kr.co.bookand.backend.policy.domain.Policy;
import kr.co.bookand.backend.policy.domain.dto.PolicyDto;

public class PolicyMockUtils {

    private static Long id = 10000L;

    public static PolicyDto createPolicyRequest() {
        return PolicyDto.builder()
                .title("title")
                .content("content")
                .build();

    }

    public static Policy getMockPolicy() {
        return createPolicyRequest().toPolicy();
    }


}
