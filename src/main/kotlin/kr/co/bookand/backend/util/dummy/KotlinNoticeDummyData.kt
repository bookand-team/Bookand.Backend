package kr.co.bookand.backend.util.dummy

import kr.co.bookand.backend.common.KotlinDeviceOSFilter
import kr.co.bookand.backend.common.KotlinMemberIdFilter
import kr.co.bookand.backend.common.KotlinStatus
import kr.co.bookand.backend.notice.domain.KotlinNotice
import kr.co.bookand.backend.notice.domain.dto.KotlinNoticeType
import kr.co.bookand.backend.notice.repository.KotlinNoticeRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Component
class KotlinNoticeDummyData(
    private val noticeRepository: KotlinNoticeRepository
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @PostConstruct
    @Transactional
    fun dummyData() {
        if (noticeRepository.count() > 0) {
            log.info("[0] 공지사항이 이미 존재하여 더미를 생성하지 않았습니다.")
            return
        }

        for (i in 1..5) {
            noticeRepository.save(
                KotlinNotice(
                    title = "공지사항 제목 $i",
                    content = "공지사항 내용 $i",
                    image = "img",
                    status = KotlinStatus.VISIBLE,
                    noticeType = KotlinNoticeType.SERVICE,
                    deviceOSFilter = KotlinDeviceOSFilter.IOS,
                    memberIdFilter = KotlinMemberIdFilter.ALL
                )
            )
        }

        for (i in 5..10) {
            noticeRepository.save(
                KotlinNotice(
                    title = "공지사항 제목 $i",
                    content = "공지사항 내용 $i",
                    image = "img",
                    status = KotlinStatus.VISIBLE,
                    noticeType = KotlinNoticeType.SERVICE,
                    deviceOSFilter = KotlinDeviceOSFilter.ANDROID,
                    memberIdFilter = KotlinMemberIdFilter.ALL
                )
            )
        }
        for (i in 10..15) {
            noticeRepository.save(
                KotlinNotice(
                    title = "공지사항 제목 $i",
                    content = "공지사항 내용 $i",
                    image = "img",
                    status = KotlinStatus.VISIBLE,
                    noticeType = KotlinNoticeType.UPDATE,
                    deviceOSFilter = KotlinDeviceOSFilter.ALL,
                    memberIdFilter = KotlinMemberIdFilter.ALL
                )
            )
        }


        for (i in 15..20) {
            noticeRepository.save(
                KotlinNotice(
                    title = "공지사항 제목 $i",
                    content = "공지사항 내용 $i",
                    image = "img",
                    status = KotlinStatus.VISIBLE,
                    noticeType = KotlinNoticeType.POLICY,
                    deviceOSFilter = KotlinDeviceOSFilter.ALL,
                    memberIdFilter = KotlinMemberIdFilter.ALL
                )
            )
        }


        for (i in 20..25) {
            noticeRepository.save(
                KotlinNotice(
                    title = "공지사항 제목 $i",
                    content = "공지사항 내용 $i",
                    image = "img",
                    status = KotlinStatus.VISIBLE,
                    noticeType = KotlinNoticeType.ETC,
                    deviceOSFilter = KotlinDeviceOSFilter.ALL,
                    memberIdFilter = KotlinMemberIdFilter.ALL
                )
            )
        }


        for (i in 25..30) {
            noticeRepository.save(
                KotlinNotice(
                    title = "공지사항 제목 $i",
                    content = "공지사항 내용 $i",
                    image = "img",
                    status = KotlinStatus.VISIBLE,
                    noticeType = KotlinNoticeType.UPDATE,
                    deviceOSFilter = KotlinDeviceOSFilter.ALL,
                    memberIdFilter = KotlinMemberIdFilter.ALL
                )
            )
        }


    }
}