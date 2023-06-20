package kr.co.bookand.backend.feedback.service

import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.common.exception.BookandException
import kr.co.bookand.backend.feedback.domain.Feedback
import kr.co.bookand.backend.feedback.domain.dto.CreateFeedbackRequest
import kr.co.bookand.backend.feedback.domain.dto.FeedbackIdResponse
import kr.co.bookand.backend.feedback.domain.dto.FeedbackListResponse
import kr.co.bookand.backend.feedback.domain.dto.FeedbackResponse
import kr.co.bookand.backend.feedback.repository.FeedbackRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class FeedbackService(
    private val feedbackRepository: FeedbackRepository,
) {

    @Transactional
    fun createFeedback(currentAccount : Account, createFeedbackRequest: CreateFeedbackRequest) : FeedbackIdResponse {
        val feedback = Feedback(createFeedbackRequest)
        val saveFeedback = feedbackRepository.save(feedback)
        return FeedbackIdResponse(saveFeedback.id)
    }

    fun getFeedback(id: Long): FeedbackResponse {
        return feedbackRepository.findById(id)
            .map { FeedbackResponse(it) }
            .orElseThrow { BookandException(ErrorCode.NOT_FOUND_FEEDBACK) }
    }

    fun getFeedbackList(pageable: Pageable): FeedbackListResponse {
        val feedbackList = feedbackRepository.findAll(pageable)
            .map { FeedbackResponse(it) }
        return FeedbackListResponse(PageResponse.of(feedbackList))
    }

}