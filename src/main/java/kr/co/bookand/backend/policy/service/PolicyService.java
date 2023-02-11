package kr.co.bookand.backend.policy.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.common.exception.ErrorCode;
import kr.co.bookand.backend.policy.domain.Policy;
import kr.co.bookand.backend.policy.domain.dto.PolicyDto;
import kr.co.bookand.backend.policy.exception.PolicyException;
import kr.co.bookand.backend.policy.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final AccountService accountService;

    @Transactional
    public PolicyDto updatePolicy(Long id, PolicyDto policyDto) {
        Account currentAccount = accountService.getCurrentAccount();
        currentAccount.getRole().checkAdmin();
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new PolicyException(ErrorCode.NOT_FOUND_POLICY, policyDto));
        policy.updateContent(policyDto.content());
        return PolicyDto.of(policy);
    }

    public PolicyDto getTitlePolicy(String title) {
        return policyRepository.findByTitle(title).map(PolicyDto::of).orElseThrow(() -> new PolicyException(ErrorCode.NOT_FOUND_POLICY, title));
    }

    public PolicyDto getPolicy(Long id) {
        return policyRepository.findById(id).map(PolicyDto::of).orElseThrow(() -> new PolicyException(ErrorCode.NOT_FOUND_POLICY, id));
    }

    @Transactional
    public PolicyDto createPolicy(PolicyDto policyDto) {
        Account currentAccount = accountService.getCurrentAccount();
        currentAccount.getRole().checkAdmin();
        Optional<Policy> policy1 = policyRepository.findByTitle(policyDto.title());
        if (policy1.isPresent()) {
            throw new PolicyException(ErrorCode.ALREADY_EXIST_POLICY, policyDto);
        }
        Policy policy = policyDto.toPolicy();
        Policy save = policyRepository.save(policy);
        return PolicyDto.of(save);
    }

    public void removePolicy(Long id) {
        Account currentAccount = accountService.getCurrentAccount();
        currentAccount.getRole().checkAdmin();
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new PolicyException(ErrorCode.NOT_FOUND_POLICY, id));
        policyRepository.delete(policy);
    }
}
