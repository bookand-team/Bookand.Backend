package kr.co.bookand.backend.policy.service


import kr.co.bookand.backend.common.exception.ErrorCode
import kr.co.bookand.backend.policy.domain.KotlinPolicy
import kr.co.bookand.backend.policy.domain.Policy
import kr.co.bookand.backend.policy.domain.dto.KotlinPolicyRequest
import kr.co.bookand.backend.policy.domain.dto.KotlinPolicyResponse
import kr.co.bookand.backend.policy.domain.dto.PolicyDto.PolicyResponse
import kr.co.bookand.backend.policy.exception.PolicyException
import kr.co.bookand.backend.policy.repository.KotlinPolicyRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.function.Function
import java.util.function.Supplier

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class KotlinPolicyService(
    private val kotlinPolicyRepository: KotlinPolicyRepository
) {
    @Transactional
    fun createPolicy(request: KotlinPolicyRequest): KotlinPolicyResponse {
        // 어드민 권한 체크
        val policy = getPolicyByName(request.name)
        if (policy != null) {
            throw IllegalArgumentException("이미 존재하는 정책입니다.")
        }
        val kotlinPolicy = KotlinPolicy(
            title = request.title,
            name = request.name,
            content = request.content
        )
        kotlinPolicyRepository.save(kotlinPolicy)
        return KotlinPolicyResponse(kotlinPolicy)
    }

    @Transactional
    fun updatePolicy(id: Long, request: KotlinPolicyRequest) : KotlinPolicyResponse {
        // 어드민 권한 체크
        val policy = getPolicyByIdOrThrow(id)
        policy.updateContent(request.content)
        return KotlinPolicyResponse(policy)
    }

    fun getTitlePolicy(name: String): KotlinPolicyResponse {
        val kotlinPolicy = getPolicyByNameOrThrow(name)
        return KotlinPolicyResponse(
            policyId = kotlinPolicy.id,
            title = kotlinPolicy.title,
            name = kotlinPolicy.name,
            content = kotlinPolicy.content
        )
    }

    fun removePolicy(id: Long) {
        // 어드민 권한 체크
        val policy = getPolicyByIdOrThrow(id)
        kotlinPolicyRepository.delete(policy)
    }

    private fun getPolicyByIdOrThrow(id: Long) : KotlinPolicy {
        return kotlinPolicyRepository.findById(id).orElse(null)
            ?: throw IllegalArgumentException("존재하지 않는 정책입니다.")
    }

    private fun getPolicyByNameOrThrow(name: String) : KotlinPolicy {
        return kotlinPolicyRepository.findByName(name)
            ?: throw IllegalArgumentException("존재하지 않는 정책입니다.")
    }

    private fun getPolicyByName(name: String) : KotlinPolicy? {
        return kotlinPolicyRepository.findByName(name)
    }
}