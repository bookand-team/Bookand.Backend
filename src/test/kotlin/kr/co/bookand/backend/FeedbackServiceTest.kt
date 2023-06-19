package kr.co.bookand.backend

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.domain.*
import kr.co.bookand.backend.feedback.domain.*
import kr.co.bookand.backend.feedback.domain.dto.KotlinCreateFeedbackRequest
import kr.co.bookand.backend.feedback.repository.KotlinFeedbackRepository
import kr.co.bookand.backend.feedback.service.KotlinFeedbackService
import java.time.LocalDateTime

class FeedbackServiceTest : BehaviorSpec({
    val feedbackRepository = mockk<KotlinFeedbackRepository>()
    val feedbackService = KotlinFeedbackService(
        feedbackRepository
    )

    Given("feedback service test"){

        val adminAccount = KotlinAccount(
            1L,
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

        val feedback = KotlinFeedback(
            1L,
            KotlinFeedbackType.PUSH,
            KotlinFeedbackTarget.HOME,
            "content",
            adminAccount
        )

        val createFeedbackRequest = KotlinCreateFeedbackRequest(
            feedbackType = KotlinFeedbackType.PUSH.name,
            feedbackTarget = KotlinFeedbackTarget.HOME.name,
            content = "content"
        )

        When("create feedback"){
            every { feedbackRepository.save(any()) } returns feedback

            val createFeedback = feedbackService.createFeedback(adminAccount, createFeedbackRequest)

            Then("it should return true"){
                createFeedback.id shouldBe 1L
            }
        }
    }
})