package kr.co.bookand.backend.policy.domain.dto;

import kr.co.bookand.backend.policy.domain.Policy;
import lombok.Builder;

public record PolicyDto(
    Long policyId,
    String title,
    String content
) {
    @Builder
    public PolicyDto {
    }

    public static PolicyDto of(Policy policy) {
        return new PolicyDto(
            policy.getId(),
            policy.getTitle(),
            policy.getContent()
        );
    }

    public Policy toPolicy() {
        return Policy.builder()
            .title(title)
            .content(content)
            .build();
    }
}


