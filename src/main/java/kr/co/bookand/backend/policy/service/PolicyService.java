package kr.co.bookand.backend.policy.service;

import kr.co.bookand.backend.policy.domain.Policy;
import kr.co.bookand.backend.policy.domain.dto.PolicyDto;
import kr.co.bookand.backend.policy.exception.NotFoundContextException;
import kr.co.bookand.backend.policy.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;

    public PolicyDto updatePolicy(PolicyDto policyDto) {
        Policy policy = policyRepository.findByTitle(policyDto.getTitle()).orElseThrow(() -> new NotFoundContextException(policyDto));
        policy.updateContext(policyDto.getContext());
        return PolicyDto.of(policy);
    }

    public PolicyDto getPolicy(String title) {
        return policyRepository.findByTitle(title).map(PolicyDto::of).orElseThrow(() -> new NotFoundContextException(title));
    }

    public void removePolicy(String title) {
        Policy policy = policyRepository.findByTitle(title).orElseThrow(() -> new NotFoundContextException(title));
        policyRepository.delete(policy);
    }
}
