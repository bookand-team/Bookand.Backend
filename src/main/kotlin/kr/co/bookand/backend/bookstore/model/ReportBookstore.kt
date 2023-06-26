package kr.co.bookand.backend.bookstore.model

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.bookstore.dto.AnswerReportRequest
import kr.co.bookand.backend.common.model.BaseEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.*

@Entity
class ReportBookstore(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_bookstore_id")
    var id: Long = 0,
    var name: String,
    val address: String,
    var isAnswered: Boolean,
    var answerTitle: String,
    var answerContent: String,
    var answeredAt: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    var account: Account? = null

) : BaseEntity() {
    fun updateAnswer(answerReportRequest: AnswerReportRequest) {
        isAnswered = true
        answerTitle = answerReportRequest.answerTitle
        answerContent = answerReportRequest.answerContent
        answeredAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
    }
}