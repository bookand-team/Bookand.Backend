package kr.co.bookand.backend.feedback.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.feedback.domain.Feedback;
import kr.co.bookand.backend.feedback.domain.FeedbackTarget;
import kr.co.bookand.backend.feedback.domain.FeedbackType;
import lombok.Builder;

public class FeedbackDto {

    public record FeedbackRequest(
            Double rating,

            @ApiModelProperty(
                    value = "피드백 타입 (HOME, MAP, BOOKMARK, MY_PAGE, ETC)",
                    example = "HOME/MAP/BOOKMARK/MY_PAGE/ETC"
            )
            String feedbackType,

            @ApiModelProperty(
                    value = "피드백 대상 (LOGIN, PUSH, UPDATE, INFORMATION_ERROR, CONTENTS, BOOKSTORE_INFO, UNKNOWN_BOOKSTORE, UXUI, ERROR, ETC)",
                    example = "LOGIN/PUSH/UPDATE/INFORMATION_ERROR/CONTENTS/BOOKSTORE_INFO/UNKNOWN_BOOKSTORE/UXUI/RROR/ETC"
            )
            String feedbackTarget,
            String content
    ) {
        @Builder
        public FeedbackRequest {
        }

        public Feedback toEntity(Account account) {
            return Feedback.builder()
                    .rating(rating)
                    .feedbackType(FeedbackType.valueOf(feedbackType))
                    .feedbackTarget(feedbackTarget == null ? null : FeedbackTarget.valueOf(feedbackTarget))
                    .content(content)
                    .account(account)
                    .build();
        }
    }

    public record FeedbackResponse(
            Long id
    ) {
        public static FeedbackResponse of(Feedback feedback) {
            return new FeedbackResponse(
                    feedback.getId()
            );
        }
    }

    public record FeedbackListResponse(
            Long id,
            String providerEmail,
            String feedbackType,
            String content,
            Double rating,
            String createdAt
    ) {
        public static FeedbackListResponse of(Feedback feedback) {
            return new FeedbackListResponse(
                    feedback.getId(),
                    feedback.getAccount().getEmail(),
                    feedback.getFeedbackType().toDetail(),
                    feedback.getContent(),
                    feedback.getRating(),
                    feedback.getCreatedAt()
            );
        }
    }

    public record FeedbackDetailResponse(
            Long id,
            String providerEmail,
            String feedbackType,
            String feedbackTarget,
            String content
    ) {
        public static FeedbackDetailResponse of(Feedback feedback) {
            return new FeedbackDetailResponse(
                    feedback.getId(),
                    feedback.getAccount().getEmail(),
                    feedback.getFeedbackType().toDetail(),
                    feedback.getFeedbackTarget() == null ? null : feedback.getFeedbackTarget().name(),
                    feedback.getContent()
            );
        }
    }
}
