package kr.co.bookand.backend.feedback.service

import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.feedback.domain.KotlinFeedback
import kr.co.bookand.backend.feedback.domain.dto.CreateFeedbackRequest
import kr.co.bookand.backend.feedback.domain.dto.FeedbackIdResponse
import kr.co.bookand.backend.feedback.repository.KotlinFeedbackRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class KotlinFeedbackService(
    private val feedbackRepository: KotlinFeedbackRepository,
    private val accountService : KotlinAccountService
) {

    @Transactional
    fun createFeedback(accountId: Long, createFeedbackRequest: CreateFeedbackRequest) : FeedbackIdResponse {
        accountService.checkAccountAdmin(accountId)
        val kotlinFeedback = KotlinFeedback(createFeedbackRequest)
        val saveFeedback = feedbackRepository.save(kotlinFeedback)
        return FeedbackIdResponse(saveFeedback.id)
    }

}