package kr.co.bookand.backend.util.dummy

import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.MemberIdFilter
import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.notice.domain.Notice
import kr.co.bookand.backend.notice.domain.NoticeType
import kr.co.bookand.backend.notice.repository.NoticeRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Component
class NoticeDummyData(
    private val noticeRepository: NoticeRepository
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
                Notice(
                    title = "공지사항 제목 $i",
                    content = "공지사항 내용 $i",
                    image = "img",
                    status = Status.VISIBLE,
                    noticeType = NoticeType.SERVICE,
                    deviceOSFilter = DeviceOSFilter.IOS,
                    memberIdFilter = MemberIdFilter.ALL
                )
            )
        }

        for (i in 5..10) {
            noticeRepository.save(
                Notice(
                    title = "공지사항 제목 $i",
                    content = "공지사항 내용 $i",
                    image = "img",
                    status = Status.VISIBLE,
                    noticeType = NoticeType.SERVICE,
                    deviceOSFilter = DeviceOSFilter.ANDROID,
                    memberIdFilter = MemberIdFilter.ALL
                )
            )
        }
        for (i in 10..15) {
            noticeRepository.save(
                Notice(
                    title = "공지사항 제목 $i",
                    content = "공지사항 내용 $i",
                    image = "img",
                    status = Status.VISIBLE,
                    noticeType = NoticeType.UPDATE,
                    deviceOSFilter = DeviceOSFilter.ALL,
                    memberIdFilter = MemberIdFilter.ALL
                )
            )
        }


        for (i in 15..20) {
            noticeRepository.save(
                Notice(
                    title = "공지사항 제목 $i",
                    content = "공지사항 내용 $i",
                    image = "img",
                    status = Status.VISIBLE,
                    noticeType = NoticeType.POLICY,
                    deviceOSFilter = DeviceOSFilter.ALL,
                    memberIdFilter = MemberIdFilter.ALL
                )
            )
        }


        for (i in 20..25) {
            noticeRepository.save(
                Notice(
                    title = "공지사항 제목 $i",
                    content = "공지사항 내용 $i",
                    image = "img",
                    status = Status.VISIBLE,
                    noticeType = NoticeType.ETC,
                    deviceOSFilter = DeviceOSFilter.ALL,
                    memberIdFilter = MemberIdFilter.ALL
                )
            )
        }


        for (i in 25..30) {
            noticeRepository.save(
                Notice(
                    title = "공지사항 제목 $i",
                    content = "공지사항 내용 $i",
                    image = "img",
                    status = Status.VISIBLE,
                    noticeType = NoticeType.UPDATE,
                    deviceOSFilter = DeviceOSFilter.ALL,
                    memberIdFilter = MemberIdFilter.ALL
                )
            )
        }


    }
}