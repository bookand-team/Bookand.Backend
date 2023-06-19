package kr.co.bookand.backend.notice.domain


import kr.co.bookand.backend.common.domain.DeviceOSFilter
import kr.co.bookand.backend.common.domain.KotlinBaseEntity
import kr.co.bookand.backend.common.domain.MemberIdFilter
import kr.co.bookand.backend.common.domain.Status
import kr.co.bookand.backend.notice.domain.dto.CreateNoticeRequest
import kr.co.bookand.backend.notice.domain.dto.NoticeDto
import kr.co.bookand.backend.notice.domain.dto.UpdateNoticeRequest
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class KotlinNotice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "knotice_id")
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

) : KotlinBaseEntity() {

    constructor(createNoticeRequest: CreateNoticeRequest) : this(
        title = createNoticeRequest.title,
        content = createNoticeRequest.content,
        image = createNoticeRequest.image,
        status = Status.INVISIBLE,
        noticeType = NoticeType.valueOf(createNoticeRequest.noticeType),
        deviceOSFilter = DeviceOSFilter.ALL,
        memberIdFilter = MemberIdFilter.ALL
    )

    fun updateNoticeData(updateNoticeRequest: UpdateNoticeRequest) {
        this.title = updateNoticeRequest.title
        this.content = updateNoticeRequest.content
        this.image = updateNoticeRequest.image
        this.noticeType = NoticeType.valueOf(updateNoticeRequest.noticeType)
        this.deviceOSFilter = DeviceOSFilter.valueOf(updateNoticeRequest.targetType)
        this.memberIdFilter = MemberIdFilter.valueOf(updateNoticeRequest.targetNum)
    }

    fun updateNoticeStatus(status: Status) {
        this.status = status
    }
}