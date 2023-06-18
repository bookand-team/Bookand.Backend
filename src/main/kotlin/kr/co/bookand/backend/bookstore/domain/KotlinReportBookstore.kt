package kr.co.bookand.backend.bookstore.domain

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.bookstore.domain.dto.KotlinAnswerReportRequest
import kr.co.bookand.backend.common.domain.KotlinBaseEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.*

@Entity
class KotlinReportBookstore(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kreport_bookstore_id")
    var id: Long = 0,
    var name: String,
    val address: String,
    var isAnswered: Boolean,
    var answerTitle: String,
    var answerContent: String,
    var answeredAt: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kaccount_id")
    var account: KotlinAccount? = null

) : KotlinBaseEntity() {
    fun updateAnswer(answerReportRequest: KotlinAnswerReportRequest) {
        isAnswered = true
        answerTitle = answerReportRequest.answerTitle
        answerContent = answerReportRequest.answerContent
        answeredAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
    }
}