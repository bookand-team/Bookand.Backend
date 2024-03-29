package kr.co.bookand.backend

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.model.*
import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.feedback.model.*
import kr.co.bookand.backend.feedback.dto.CreateFeedbackRequest
import kr.co.bookand.backend.feedback.repository.FeedbackRepository
import kr.co.bookand.backend.feedback.service.FeedbackService
import java.time.LocalDateTime

class FeedbackServiceTest : BehaviorSpec({
    val feedbackRepository = mockk<FeedbackRepository>()
    val feedbackService = FeedbackService(
        feedbackRepository
    )

    Given("feedback service test"){

        val adminAccount = Account(
            1L,
            "admin@email.com",
            "password",
            "admin",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            Role.ADMIN,
            AccountStatus.NORMAL
        )

        val feedback = Feedback(
            1L,
            FeedbackType.PUSH,
            FeedbackTarget.HOME,
            "content",
            adminAccount,
            DeviceOSFilter.ANDROID,
            false
        )

        val createFeedbackRequest = CreateFeedbackRequest(
            feedbackType = FeedbackType.PUSH.name,
            feedbackTarget = FeedbackTarget.HOME.name,
            content = "content",
            deviceOS = DeviceOSFilter.ANDROID
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