package kr.co.bookand.backend.feedback.service

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.common.KotlinPageResponse
import kr.co.bookand.backend.feedback.domain.KotlinFeedback
import kr.co.bookand.backend.feedback.domain.dto.KotlinCreateFeedbackRequest
import kr.co.bookand.backend.feedback.domain.dto.KotlinFeedbackIdResponse
import kr.co.bookand.backend.feedback.domain.dto.KotlinFeedbackListResponse
import kr.co.bookand.backend.feedback.domain.dto.KotlinFeedbackResponse
import kr.co.bookand.backend.feedback.repository.KotlinFeedbackRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class KotlinFeedbackService(
    private val feedbackRepository: KotlinFeedbackRepository,
) {

    @Transactional
    fun createFeedback(currentAccount : KotlinAccount, createFeedbackRequest: KotlinCreateFeedbackRequest) : KotlinFeedbackIdResponse {
        val kotlinFeedback = KotlinFeedback(createFeedbackRequest)
        val saveFeedback = feedbackRepository.save(kotlinFeedback)
        return KotlinFeedbackIdResponse(saveFeedback.id)
    }

    fun getFeedback(id: Long): KotlinFeedbackResponse {
        return feedbackRepository.findById(id)
            .map { KotlinFeedbackResponse(it) }
            .orElseThrow { IllegalArgumentException("해당 피드백이 존재하지 않습니다.") }
    }

    fun getFeedbackList(pageable: Pageable): KotlinFeedbackListResponse {
        val feedbackList = feedbackRepository.findAll(pageable)
            .map { KotlinFeedbackResponse(it) }
        return KotlinFeedbackListResponse(KotlinPageResponse.of(feedbackList))
    }

}