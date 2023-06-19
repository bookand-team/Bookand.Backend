package kr.co.bookand.backend.policy.service


import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.policy.domain.KotlinPolicy
import kr.co.bookand.backend.policy.domain.dto.KotlinPolicyIdResponse
import kr.co.bookand.backend.policy.domain.dto.KotlinPolicyRequest
import kr.co.bookand.backend.policy.domain.dto.KotlinPolicyResponse
import kr.co.bookand.backend.policy.repository.KotlinPolicyRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class KotlinPolicyService(
    private val kotlinPolicyRepository: KotlinPolicyRepository
) {
    @Transactional
    fun createPolicy(currentAccount: KotlinAccount, request: KotlinPolicyRequest): KotlinPolicyIdResponse {
        currentAccount.role.checkAdminAndManager()
        val policy = getPolicyByName(request.name)
        if (policy != null) {
            throw IllegalArgumentException("이미 존재하는 정책입니다.")
        }
        val kotlinPolicy = KotlinPolicy(
            title = request.title,
            name = request.name,
            content = request.content
        )
        val savePolicy = kotlinPolicyRepository.save(kotlinPolicy)
        return KotlinPolicyIdResponse(savePolicy.id)
    }

    @Transactional
    fun updatePolicy(
        currentAccount: KotlinAccount,
        id: Long,
        request: KotlinPolicyRequest
    ): KotlinPolicyIdResponse {
        currentAccount.role.checkAdminAndManager()
        val policy = getPolicyByIdOrThrow(id)
        policy.updateContent(request.content)
        return KotlinPolicyIdResponse(policy.id)
    }

    fun getTitlePolicy(name: String): KotlinPolicyResponse {
        val kotlinPolicy = getPolicyByNameOrThrow(name)
        return KotlinPolicyResponse(kotlinPolicy)
    }

    fun removePolicy(currentAccount: KotlinAccount, id: Long) {
        currentAccount.role.checkAdminAndManager()
        val policy = getPolicyByIdOrThrow(id)
        kotlinPolicyRepository.delete(policy)
    }

    private fun getPolicyByIdOrThrow(id: Long): KotlinPolicy {
        return kotlinPolicyRepository.findById(id).orElse(null)
            ?: throw IllegalArgumentException("존재하지 않는 정책입니다.")
    }

    private fun getPolicyByNameOrThrow(name: String): KotlinPolicy {
        return kotlinPolicyRepository.findByName(name)
            ?: throw IllegalArgumentException("존재하지 않는 정책입니다.")
    }

    private fun getPolicyByName(name: String): KotlinPolicy? {
        return kotlinPolicyRepository.findByName(name)
    }
}