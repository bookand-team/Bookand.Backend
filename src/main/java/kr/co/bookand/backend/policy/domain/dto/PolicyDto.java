package kr.co.bookand.backend.policy.domain.dto;

import kr.co.bookand.backend.policy.domain.Policy;
import lombok.Builder;

public class PolicyDto {


    public record PolicyRequest(
            Long policyId,
            String title,
            String name,
            String content
    ) {
        @Builder
        public PolicyRequest {
        }

        public Policy toPolicy() {
            return Policy.builder()
                    .title(title)
                    .name(name)
                    .content(content)
                    .build();
        }
    }

    public record PolicyResponse(
            Long policyId,
            String title,
            String content
    ) {
        @Builder
        public PolicyResponse {
        }

        public static PolicyResponse of(Policy policy) {
            return PolicyResponse.builder()
                    .policyId(policy.getId())
                    .title(policy.getTitle())
                    .content(policy.getContent())
                    .build();
        }
    }
}


