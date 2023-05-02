package kr.co.bookand.backend

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.policy.domain.KotlinPolicy
import kr.co.bookand.backend.policy.domain.dto.KotlinPolicyRequest
import kr.co.bookand.backend.policy.repository.KotlinPolicyRepository
import kr.co.bookand.backend.policy.service.KotlinPolicyService
import java.util.*

// test
class PolicyServiceTest : BehaviorSpec({
    val policyRepository = mockk<KotlinPolicyRepository>()
    val policyService = KotlinPolicyService(policyRepository)

    Given("policy Test") {
        val policyId = 1L
        val policy = KotlinPolicy(
            1L,
            "Privacy Policy",
            "This is privacy policy",
            "This is privacy policy content"
        )

        val policy2 = KotlinPolicy(
            2L,
            "Privacy Policy2",
            "This is privacy policy2",
            "This is privacy policy content2"
        )
        every { policyRepository.findById(1) } returns Optional.of(policy)
        When("policy 업데이트") {
            val request = KotlinPolicyRequest(
                1L,
                "Privacy Policy",
                "This is privacy policy",
                "This is new privacy policy content"
            )
            val updatedPolicy = policyService.updatePolicy(policyId, request)
            Then("it should update the content of the policy") {
                updatedPolicy.content shouldBe request.content
            }
        }
        When("policy 검색 by title") {
            every { policyRepository.findByName("This is privacy policy") } returns policy
            val result = policyService.getTitlePolicy("This is privacy policy")
            Then("it should return the policy with the given id") {
                result.policyId shouldBe policy.id
                result.name shouldBe policy.name
                result.content shouldBe policy.content
                result.title shouldBe policy.title
            }
        }
        When("policy 검색 by name") {
            every { policyRepository.findByName(policy.name) } returns policy
            val result = policyService.getTitlePolicy(policy.name)
            Then("it should return the policy with the given name") {
                result.policyId shouldBe policy.id
                result.name shouldBe policy.name
                result.content shouldBe policy.content
            }
        }
        When("policy 생성") {
            val request = KotlinPolicyRequest(
                2L,
                "This is privacy policy2",
                "Privacy Policy2",
                "This is privacy policy content2"
            )
            every { policyRepository.findByName("Privacy Policy2") } returns null
            every { policyRepository.save(any()) } returns policy2
            val result = policyService.createPolicy(request)

            Then("it should create the policy") {
                result.title shouldBe policy2.title
                result.name shouldBe policy2.name
                result.content shouldBe policy2.content
            }
        }
        When("policy 삭제") {
            every { policyRepository.findById(policyId) } returns Optional.of(policy)
            every { policyRepository.delete(policy) } returns Unit
            policyService.removePolicy(policyId)
            Then("it should remove the policy") {
                every { policyRepository.findById(policyId) } returns Optional.empty()
                policyRepository.findById(policyId) shouldBe Optional.empty()
            }
        }
    }
})