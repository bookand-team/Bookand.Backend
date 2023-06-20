package kr.co.bookand.backend.policy.service


import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.policy.domain.Policy
import kr.co.bookand.backend.policy.domain.dto.PolicyIdResponse
import kr.co.bookand.backend.policy.domain.dto.CreatePolicyRequest
import kr.co.bookand.backend.policy.domain.dto.PolicyResponse
import kr.co.bookand.backend.policy.repository.PolicyRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class PolicyService(
    private val policyRepository: PolicyRepository
) {
    @Transactional
    fun createPolicy(currentAccount: Account, request: CreatePolicyRequest): PolicyIdResponse {
        currentAccount.role.checkAdminAndManager()
        val policy = getPolicyByName(request.name)
        if (policy != null) {
            throw IllegalArgumentException("이미 존재하는 정책입니다.")
        }
        val newPolicy = Policy(
            title = request.title,
            name = request.name,
            content = request.content
        )
        val savePolicy = policyRepository.save(newPolicy)
        return PolicyIdResponse(savePolicy.id)
    }

    @Transactional
    fun updatePolicy(
        currentAccount: Account,
        id: Long,
        request: CreatePolicyRequest
    ): PolicyIdResponse {
        currentAccount.role.checkAdminAndManager()
        val policy = getPolicyByIdOrThrow(id)
        policy.updateContent(request.content)
        return PolicyIdResponse(policy.id)
    }

    fun getTitlePolicy(name: String): PolicyResponse {
        val policy = getPolicyByNameOrThrow(name)
        return PolicyResponse(policy)
    }

    fun removePolicy(currentAccount: Account, id: Long) {
        currentAccount.role.checkAdminAndManager()
        val policy = getPolicyByIdOrThrow(id)
        policyRepository.delete(policy)
    }

    private fun getPolicyByIdOrThrow(id: Long): Policy {
        return policyRepository.findById(id).orElse(null)
            ?: throw IllegalArgumentException("존재하지 않는 정책입니다.")
    }

    private fun getPolicyByNameOrThrow(name: String): Policy {
        return policyRepository.findByName(name)
            ?: throw IllegalArgumentException("존재하지 않는 정책입니다.")
    }

    private fun getPolicyByName(name: String): Policy? {
        return policyRepository.findByName(name)
    }
}