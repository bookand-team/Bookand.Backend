package kr.co.bookand.backend

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.domain.*
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.feedback.domain.FeedbackTarget
import kr.co.bookand.backend.feedback.domain.FeedbackType
import kr.co.bookand.backend.feedback.domain.KotlinFeedback
import kr.co.bookand.backend.feedback.domain.dto.CreateFeedbackRequest
import kr.co.bookand.backend.feedback.repository.KotlinFeedbackRepository
import kr.co.bookand.backend.feedback.service.KotlinFeedbackService
import java.time.LocalDateTime

class FeedbackServiceTest : BehaviorSpec({
    val feedbackRepository = mockk<KotlinFeedbackRepository>()
    val accountService = mockk< KotlinAccountService>()
    val feedbackService = KotlinFeedbackService(
        feedbackRepository,
        accountService
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
            FeedbackType.PUSH,
            FeedbackTarget.HOME,
            "content",
            adminAccount
        )

        val createFeedbackRequest = CreateFeedbackRequest(
            feedbackType = FeedbackType.PUSH.name,
            feedbackTarget = FeedbackTarget.HOME.name,
            content = "content"
        )

        When("create feedback"){
            every { accountService.checkAccountAdmin(1L) } returns Unit
            every { feedbackRepository.save(any()) } returns feedback

            val createFeedback = feedbackService.createFeedback(1L, createFeedbackRequest)

            Then("it should return true"){
                createFeedback.id shouldBe 1L
            }
        }
    }
})