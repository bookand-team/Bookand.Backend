package kr.co.bookand.backend.notice.model


import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.MemberIdFilter
import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.common.model.BaseEntity
import kr.co.bookand.backend.notice.dto.CreateNoticeRequest
import kr.co.bookand.backend.notice.dto.UpdateNoticeRequest
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Notice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    var id: Long = 0,

    var title: String,
    var content: String,
    var image: String,
    @Enumerated(EnumType.STRING)
    var status: Status,

    @Enumerated(EnumType.STRING)
    var noticeType: NoticeType,

    @Enumerated(EnumType.STRING)
    var deviceOSFilter: DeviceOSFilter,

    @Enumerated(EnumType.STRING)
    var memberIdFilter: MemberIdFilter,

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    var displayAt: LocalDateTime? = null

) : BaseEntity() {

    constructor(createNoticeRequest: CreateNoticeRequest) : this(
        title = createNoticeRequest.title,
        content = createNoticeRequest.content,
        image = createNoticeRequest.image,
        status = Status.INVISIBLE,
        noticeType = NoticeType.valueOf(createNoticeRequest.noticeType),
        deviceOSFilter = createNoticeRequest.noticeFilter.deviceOS,
        memberIdFilter = createNoticeRequest.noticeFilter.memberId
    )

    fun updateNoticeData(updateNoticeRequest: UpdateNoticeRequest) {
        this.title = updateNoticeRequest.title
        this.content = updateNoticeRequest.content
        this.image = updateNoticeRequest.image
        this.noticeType = NoticeType.valueOf(updateNoticeRequest.noticeType)
        this.deviceOSFilter = updateNoticeRequest.noticeFilter.deviceOS
        this.memberIdFilter = updateNoticeRequest.noticeFilter.memberId
    }

    fun updateNoticeStatus(status: Status) {
        this.status = status
        this.displayAt = LocalDateTime.now()
    }

    fun updateDisplayAt() {
        this.displayAt = LocalDateTime.now()
    }
}