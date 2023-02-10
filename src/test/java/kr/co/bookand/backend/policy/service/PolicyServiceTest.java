package kr.co.bookand.backend.policy.service;

import kr.co.bookand.backend.policy.domain.Policy;
import kr.co.bookand.backend.policy.domain.dto.PolicyDto;
import kr.co.bookand.backend.policy.repository.PolicyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

    @Mock
    PolicyRepository policyRepository;

    @InjectMocks
    PolicyService policyService;


    @Test
    @DisplayName("정책 조회")
    void getPolicy() {
        // given
        PolicyDto policyRequest = PolicyMockUtils.createPolicyRequest();
        Policy policy = policyRequest.toPolicy();
        given(policyRepository.findById(any())).willReturn(Optional.of(policy));

        //when
        PolicyDto policyDto = policyService.getPolicy(1L);

        //then
        assertEquals(policyDto.title(), policyRequest.title());
        assertEquals(policyDto.context(), policyRequest.context());
    }

    @Test
    @DisplayName("정책 생성")
    void createPolicy() {
        // given
        PolicyDto policyRequest = PolicyMockUtils.createPolicyRequest();
        Policy policy = policyRequest.toPolicy();
        given(policyRepository.save(any())).willReturn(policy);

        //when
        PolicyDto policyDto = policyService.createPolicy(policyRequest);

        //then
        assertEquals(policyDto.title(), policyRequest.title());
        assertEquals(policyDto.context(), policyRequest.context());
    }

    @Test
    @DisplayName("정책 수정")
    void updatePolicy() {
        // given
        PolicyDto policyRequest = PolicyMockUtils.createPolicyRequest();
        Policy policy = policyRequest.toPolicy();
        given(policyRepository.findByTitle(any())).willReturn(Optional.of(policy));

        //when
        PolicyDto policyDto = policyService.updatePolicy(1L, policyRequest);

        //then
        assertEquals(policyDto.title(), policyRequest.title());
        assertEquals(policyDto.context(), policyRequest.context());
    }

    @Test
    @DisplayName("정책 삭제")
    void removePolicy() {
        // given
        PolicyDto policyRequest = PolicyMockUtils.createPolicyRequest();
        Policy policy = policyRequest.toPolicy();
        given(policyRepository.findById(any())).willReturn(Optional.of(policy));

        //when
        policyService.removePolicy(1L);

        //then
        assertTrue(true);
    }

}