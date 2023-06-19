package kr.co.bookand.backend

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.account.domain.KotlinAccountStatus
import kr.co.bookand.backend.account.domain.KotlinRole
import kr.co.bookand.backend.policy.domain.KotlinPolicy
import kr.co.bookand.backend.policy.domain.dto.KotlinPolicyRequest
import kr.co.bookand.backend.policy.repository.KotlinPolicyRepository
import kr.co.bookand.backend.policy.service.KotlinPolicyService
import java.time.LocalDateTime
import java.util.*

class PolicyServiceTest : BehaviorSpec({
    val policyRepository = mockk<KotlinPolicyRepository>()
    val policyService = KotlinPolicyService(policyRepository)

    Given("policy Test") {

        val account = KotlinAccount(
            1L,
            "email",
            "password",
            "name",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            KotlinRole.USER,
            KotlinAccountStatus.NORMAL
        )

        val adminAccount = KotlinAccount(
            2L,
            "admin@email.com",
            "password",
            "admin",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            KotlinRole.ADMIN,
            KotlinAccountStatus.NORMAL
        )

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
            val updatedPolicy = policyService.updatePolicy(adminAccount, policyId, request)
            Then("it should update the content of the policy") {
                updatedPolicy.id shouldBe 1L
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
            val result = policyService.createPolicy(adminAccount, request)

            Then("it should create the policy") {
                result.id shouldBe 2L
            }
        }
        When("policy 삭제") {
            every { policyRepository.findById(policyId) } returns Optional.of(policy)
            every { policyRepository.delete(policy) } returns Unit
            policyService.removePolicy(adminAccount, policyId)
            Then("it should remove the policy") {
                every { policyRepository.findById(policyId) } returns Optional.empty()
                policyRepository.findById(policyId) shouldBe Optional.empty()
            }
        }
    }
})