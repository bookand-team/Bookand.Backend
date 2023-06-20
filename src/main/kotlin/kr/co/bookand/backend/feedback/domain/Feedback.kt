package kr.co.bookand.backend.feedback.domain

import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.common.domain.BaseEntity
import kr.co.bookand.backend.feedback.domain.dto.CreateFeedbackRequest
import org.springframework.lang.Nullable
import javax.persistence.*

@Entity
class Feedback(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    var id: Long = 0,

    @Enumerated(EnumType.STRING)
    var feedbackType: FeedbackType,

    @Enumerated(EnumType.STRING)
    @Nullable
    var feedbackTarget: FeedbackTarget,

    @Column(length = 1000)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    var account: Account? = null

) : BaseEntity() {

    constructor(createFeedbackRequest: CreateFeedbackRequest) : this(
        feedbackType = FeedbackType.valueOf(createFeedbackRequest.feedbackType),
        feedbackTarget = FeedbackTarget.valueOf(createFeedbackRequest.feedbackTarget),
        content = createFeedbackRequest.content,
    )
}