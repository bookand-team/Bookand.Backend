package kr.co.bookand.backend.policy.service;

import kr.co.bookand.backend.AccountTemplate;
import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.policy.domain.Policy;
import kr.co.bookand.backend.policy.repository.PolicyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static kr.co.bookand.backend.policy.domain.dto.PolicyDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

    @Mock
    AccountService accountService;

    @Mock
    PolicyRepository policyRepository;

    @InjectMocks
    PolicyService policyService;


    @Test
    @DisplayName("정책 조회")
    void getPolicy() {
        // given
        PolicyRequest policyRequest = PolicyMockUtils.createPolicyRequest();
        Policy policy = policyRequest.toPolicy();
        given(policyRepository.findById(any())).willReturn(Optional.of(policy));

        //when
        PolicyResponse policyResponse = policyService.getPolicy(1L);

        //then
        assertEquals(policyResponse.title(), policyRequest.title());
        assertEquals(policyResponse.content(), policyRequest.content());
    }

    @Test
    @DisplayName("정책 생성")
    void createPolicy() {
        // given
        Account account = AccountTemplate.makeAccount1();
        given(accountService.getCurrentAccount()).willReturn(account);

        PolicyRequest policyRequest = PolicyMockUtils.createPolicyRequest();
        Policy mockPolicy = PolicyMockUtils.getMockPolicy();

        given(policyRepository.save(any())).willReturn(mockPolicy);

        //when
        PolicyResponse policyResponse = policyService.createPolicy(policyRequest);

        //then
        assertEquals(mockPolicy.getTitle(), policyResponse.title());
        assertEquals(mockPolicy.getContent(), policyResponse.content());
    }

    @Test
    @DisplayName("정책 수정")
    void updatePolicy() {
        // given
        Account account = AccountTemplate.makeAccount1();
        given(accountService.getCurrentAccount()).willReturn(account);

        PolicyRequest policyRequest = PolicyMockUtils.createPolicyRequest();
        Policy policy = policyRequest.toPolicy();
        given(policyRepository.findById(any())).willReturn(Optional.of(policy));

        //when
        PolicyResponse policyResponse = policyService.updatePolicy(policy.getId(), policyRequest);

        //then
        assertEquals(policyResponse.title(), policyRequest.title());
        assertEquals(policyResponse.content(), policyRequest.content());
    }

    @Test
    @DisplayName("정책 삭제")
    void removePolicy() {
        // given
        Account account = AccountTemplate.makeAccount1();
        given(accountService.getCurrentAccount()).willReturn(account);

        PolicyRequest policyRequest = PolicyMockUtils.createPolicyRequest();
        Policy policy = policyRequest.toPolicy();
        given(policyRepository.findById(any())).willReturn(Optional.of(policy));

        //when
        policyService.removePolicy(1L);

        //then
        assertTrue(true);
    }

}