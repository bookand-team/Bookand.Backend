package kr.co.bookand.backend.policy.domain.dto;

import kr.co.bookand.backend.common.BaseTimeEntity;
import kr.co.bookand.backend.policy.domain.Policy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PolicyDto extends BaseTimeEntity {
    String title;
    String context;

    public static PolicyDto of(Policy policy) {
        return PolicyDto.builder()
                .title(policy.getTitle())
                .context(policy.getContext())
                .build();
    }
}


