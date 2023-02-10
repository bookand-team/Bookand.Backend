package kr.co.bookand.backend.policy.domain.dto;

import kr.co.bookand.backend.policy.domain.Policy;
import lombok.Builder;

public record PolicyDto(
    Long policyId,
    String title,
    String context
) {
    @Builder
    public PolicyDto {
    }

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


