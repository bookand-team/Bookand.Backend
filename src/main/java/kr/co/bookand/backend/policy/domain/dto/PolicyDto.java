package kr.co.bookand.backend.policy.domain.dto;

import kr.co.bookand.backend.policy.domain.Policy;

public record PolicyDto(
    Long policyId,
    String title,
    String context
) {
    public static PolicyDto of(Policy policy) {
        return new PolicyDto(
            policy.getId(),
            policy.getTitle(),
            policy.getContext()
        );
    }

    public Policy toPolicy() {
        return Policy.builder()
            .title(title)
            .context(context)
            .build();
    }
}


