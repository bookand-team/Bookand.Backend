package kr.co.bookand.backend.feedback.model

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.model.BaseEntity
import kr.co.bookand.backend.feedback.dto.CreateFeedbackRequest
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
    var feedbackTarget: FeedbackTarget? = null,

    @Column(length = 1000)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    var account: Account? = null,

    @Enumerated(EnumType.STRING)
    var deviceOS: DeviceOSFilter,

    var isConfirmed: Boolean,
) : BaseEntity() {

    constructor(createFeedbackRequest: CreateFeedbackRequest) : this(
        feedbackType = FeedbackType.valueOf(createFeedbackRequest.feedbackType),
        content = createFeedbackRequest.content,
        deviceOS = createFeedbackRequest.deviceOS,
        isConfirmed = false
    )

    fun updateFeedbackTarget(feedbackTarget: FeedbackTarget) {
        this.feedbackTarget = feedbackTarget
    }

    fun updateFeedbackAccount(account: Account) {
        this.account = account
    }

    fun updateConfirmed(updateConfirmed: Boolean) {
        this.isConfirmed = updateConfirmed
    }
}