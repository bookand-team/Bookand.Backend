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

import static kr.co.bookand.backend.policy.domain.dto.PolicyDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final AccountService accountService;

    @Transactional
    public PolicyResponse updatePolicy(Long id, PolicyRequest request) {
        Account currentAccount = accountService.getCurrentAccount();
        currentAccount.getRole().checkAdmin();
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new PolicyException(ErrorCode.NOT_FOUND_POLICY, request));
        policy.updateContent(request.content());
        return PolicyResponse.of(policy);
    }

    public PolicyResponse getTitlePolicy(String name) {
        return policyRepository.findByName(name).map(PolicyResponse::of).orElseThrow(() -> new PolicyException(ErrorCode.NOT_FOUND_POLICY, name));
    }

    public PolicyResponse getPolicy(Long id) {
        return policyRepository.findById(id).map(PolicyResponse::of).orElseThrow(() -> new PolicyException(ErrorCode.NOT_FOUND_POLICY, id));
    }

    @Transactional
    public PolicyResponse createPolicy(PolicyRequest request) {
        Account currentAccount = accountService.getCurrentAccount();
        currentAccount.getRole().checkAdmin();
        Optional<Policy> policyResponse = policyRepository.findByName(request.name());
        if (policyResponse.isPresent()) {
            throw new PolicyException(ErrorCode.ALREADY_EXIST_POLICY, request);
        }
        Policy policy = request.toPolicy();
        Policy save = policyRepository.save(policy);
        return PolicyResponse.of(save);
    }

    public void removePolicy(Long id) {
        Account currentAccount = accountService.getCurrentAccount();
        currentAccount.getRole().checkAdmin();
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new PolicyException(ErrorCode.NOT_FOUND_POLICY, id));
        policyRepository.delete(policy);
    }
}
