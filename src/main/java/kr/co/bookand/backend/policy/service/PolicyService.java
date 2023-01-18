package kr.co.bookand.backend.policy.service;

import kr.co.bookand.backend.common.exception.ErrorCode;
import kr.co.bookand.backend.policy.domain.Policy;
import kr.co.bookand.backend.policy.domain.dto.PolicyDto;
import kr.co.bookand.backend.policy.exception.PolicyException;
import kr.co.bookand.backend.policy.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;

    public PolicyDto updatePolicy(PolicyDto policyDto) {
        Policy policy = policyRepository.findByTitle(policyDto.getTitle()).orElseThrow(() -> new PolicyException(ErrorCode.NOT_FOUND_POLICY, policyDto));
        policy.updateContext(policyDto.getContext());
        return PolicyDto.of(policy);
    }

    public PolicyDto getTitlePolicy(String title) {
        return policyRepository.findByTitle(title).map(PolicyDto::of).orElseThrow(() -> new PolicyException(ErrorCode.NOT_FOUND_POLICY, title));
    }

    public PolicyDto getPolicy(Long id) {
        return policyRepository.findById(id).map(PolicyDto::of).orElseThrow(() -> new PolicyException(ErrorCode.NOT_FOUND_POLICY, id));
    }

    public PolicyDto createPolicy(PolicyDto policyDto) {
        Optional<Policy> policy1 = policyRepository.findByTitle(policyDto.getTitle());
        if (policy1.isPresent()) {
            throw new RuntimeException();
        }
        Policy policy = policyDto.toPolicy();
        Policy save = policyRepository.save(policy);
        return PolicyDto.of(save);
    }

    public void removePolicy(Long id) {
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new PolicyException(ErrorCode.NOT_FOUND_POLICY, id));
        policyRepository.delete(policy);
    }
}
