package kr.co.bookand.backend.feedback.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import kr.co.bookand.backend.common.exception.ErrorCode;
import kr.co.bookand.backend.feedback.domain.Feedback;
import kr.co.bookand.backend.feedback.domain.dto.FeedbackDto.*;
import kr.co.bookand.backend.feedback.exception.FeedbackException;
import kr.co.bookand.backend.feedback.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedbackService {

    private final AccountService accountService;
    private final FeedbackRepository feedbackRepository;

        @Transactional
        public FeedbackResponse createFeedback(FeedbackRequest feedbackRequest) {
            Account account = accountService.checkAccountUser();
            Feedback feedback = feedbackRequest.toEntity(account);
            feedbackRepository.save(feedback);
            return FeedbackResponse.of(feedback);
        }

        public PageResponse<FeedbackListResponse> getFeedbackList(Pageable pageable) {
            accountService.isAccountAdmin();
            Page<FeedbackListResponse> feedbackList = feedbackRepository.findAll(pageable).map(FeedbackListResponse::of);
            return PageResponse.of(feedbackList);
        }

        public FeedbackDetailResponse getFeedbackDetail(Long feedbackId) {
            accountService.isAccountAdmin();
            Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(() -> new FeedbackException(ErrorCode.NOT_FOUND_FEEDBACK, feedbackId));
            return FeedbackDetailResponse.of(feedback);
        }

}
