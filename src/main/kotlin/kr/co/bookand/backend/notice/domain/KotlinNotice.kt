package kr.co.bookand.backend.notice.domain


import kr.co.bookand.backend.common.KotlinDeviceOSFilter
import kr.co.bookand.backend.common.KotlinMemberIdFilter
import kr.co.bookand.backend.common.KotlinStatus
import kr.co.bookand.backend.common.domain.KotlinBaseEntity
import kr.co.bookand.backend.notice.domain.dto.CreateNoticeRequest
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
    var status: KotlinStatus,

    @Enumerated(EnumType.STRING)
    var noticeType: NoticeType,

    @Enumerated(EnumType.STRING)
    var deviceOSFilter: KotlinDeviceOSFilter,

    @Enumerated(EnumType.STRING)
    var memberIdFilter: KotlinMemberIdFilter,

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    var displayAt: LocalDateTime? = null

) : KotlinBaseEntity() {

    constructor(createNoticeRequest: CreateNoticeRequest) : this(
        title = createNoticeRequest.title,
        content = createNoticeRequest.content,
        image = createNoticeRequest.image,
        status = KotlinStatus.INVISIBLE,
        noticeType = NoticeType.valueOf(createNoticeRequest.noticeType),
        deviceOSFilter = KotlinDeviceOSFilter.ALL,
        memberIdFilter = KotlinMemberIdFilter.ALL
    )

    fun updateNoticeData(updateNoticeRequest: UpdateNoticeRequest) {
        this.title = updateNoticeRequest.title
        this.content = updateNoticeRequest.content
        this.image = updateNoticeRequest.image
        this.noticeType = NoticeType.valueOf(updateNoticeRequest.noticeType)
        this.deviceOSFilter = KotlinDeviceOSFilter.valueOf(updateNoticeRequest.targetType)
        this.memberIdFilter = KotlinMemberIdFilter.valueOf(updateNoticeRequest.targetNum)
    }

    fun updateNoticeStatus(status: KotlinStatus) {
        this.status = status
    }
}