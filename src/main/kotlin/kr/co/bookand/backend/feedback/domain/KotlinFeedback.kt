package kr.co.bookand.backend.feedback.domain

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.common.domain.KotlinBaseEntity
import kr.co.bookand.backend.feedback.domain.dto.KotlinCreateFeedbackRequest
import org.springframework.lang.Nullable
import javax.persistence.*

@Entity
class KotlinFeedback(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kfeedback_id")
    var id: Long = 0,

    @Enumerated(EnumType.STRING)
    var feedbackType: KotlinFeedbackType,

    @Enumerated(EnumType.STRING)
    @Nullable
    var feedbackTarget: KotlinFeedbackTarget,

    @Column(length = 1000)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kaccount_id")
    var account: KotlinAccount? = null

) : KotlinBaseEntity() {

    constructor(createFeedbackRequest: KotlinCreateFeedbackRequest) : this(
        feedbackType = KotlinFeedbackType.valueOf(createFeedbackRequest.feedbackType),
        feedbackTarget = KotlinFeedbackTarget.valueOf(createFeedbackRequest.feedbackTarget),
        content = createFeedbackRequest.content,
    )
}