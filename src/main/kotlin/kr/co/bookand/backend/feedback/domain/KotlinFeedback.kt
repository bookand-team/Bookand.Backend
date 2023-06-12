package kr.co.bookand.backend.feedback.domain

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.common.domain.KotlinBaseEntity
import kr.co.bookand.backend.feedback.domain.dto.CreateFeedbackRequest
import org.springframework.lang.Nullable
import javax.persistence.*

@Entity
class KotlinFeedback(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kfeedback_id")
    var id: Long = 0,

    @Enumerated(EnumType.STRING)
    var feedbackType: FeedbackType,

    @Enumerated(EnumType.STRING)
    @Nullable
    var feedbackTarget: FeedbackTarget,

    @Column(length = 1000)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kaccount_id")
    var account: KotlinAccount? = null

) : KotlinBaseEntity() {

    constructor(createFeedbackRequest: CreateFeedbackRequest) : this(
        feedbackType = FeedbackType.valueOf(createFeedbackRequest.feedbackType),
        feedbackTarget = FeedbackTarget.valueOf(createFeedbackRequest.feedbackTarget),
        content = createFeedbackRequest.content,
    )
}