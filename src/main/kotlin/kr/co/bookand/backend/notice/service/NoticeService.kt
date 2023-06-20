package kr.co.bookand.backend.notice.service

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.common.model.MessageResponse
import kr.co.bookand.backend.notice.dto.*
import kr.co.bookand.backend.notice.model.Notice
import kr.co.bookand.backend.notice.repository.NoticeRepository
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
class NoticeService(
    private val noticeRepository: NoticeRepository
) {

    @Transactional
    fun createNotice(
        currentAccount: Account,
        createNoticeRequest: CreateNoticeRequest
    ): NoticeIdResponse {
        currentAccount.role.checkAdminAndManager()
        val notice = Notice(createNoticeRequest)
        val saveNotice = noticeRepository.save(notice)
        return NoticeIdResponse(saveNotice.id)
    }

    fun getNoticeSimpleList(
        pageable: Pageable,
        cursorId: Long?
    ): NoticePageResponse {
        val nextCursorId = cursorId ?: 0L
        val page = noticeRepository.findAllByVisibilityAndStatus(pageable, true, Status.VISIBLE, nextCursorId)
            .map { NoticeResponse(it) }
        val totalElements = noticeRepository.count()
        val ofCursor = PageResponse.ofCursor(page, totalElements)
        return NoticePageResponse(ofCursor)
    }

    @Transactional
    fun updateNotice(
        currentAccount: Account,
        noticeId: Long,
        updateNoticeRequest: UpdateNoticeRequest
    ): NoticeIdResponse {
        currentAccount.role.checkAdminAndManager()
        val notice = getNotice(noticeId)
        notice.updateNoticeData(updateNoticeRequest)
        return NoticeIdResponse(notice.id)
    }

    @Transactional
    fun deleteNotice(currentAccount: Account, noticeId: Long): MessageResponse {
        currentAccount.role.checkAdminAndManager()
        noticeRepository.deleteById(noticeId)
        return MessageResponse(message = "Success", statusCode = 200)
    }

    @Transactional
    fun updateNoticeStatus(
        currentAccount: Account,
        noticeId: Long
    ): MessageResponse {
        currentAccount.role.checkAdminAndManager()
        val notice = getNotice(noticeId)
        return if (notice.status == Status.INVISIBLE) {
            notice.status = Status.VISIBLE
            MessageResponse(message = "Update Status to Visible.", statusCode = 200)
        } else {
            notice.status = Status.INVISIBLE
            MessageResponse(message = "Update Status to Invisible.", statusCode = 200)
        }
    }

    fun getNoticeDetail(noticeId: Long): NoticeResponse {
        val notice = getNotice(noticeId)
        return NoticeResponse(notice)
    }

    fun getNoticeList(
        pageable: Pageable
    ): NoticePageResponse {
        val list = noticeRepository.findAllByStatusAndVisibility(pageable, Status.VISIBLE, true)
            .map { NoticeResponse(it) }
        return NoticePageResponse(PageResponse.of(list))
    }

    fun getNotice(noticeId: Long): Notice {
        return noticeRepository.findById(noticeId).orElseThrow { throw Exception("존재하지 않는 공지사항입니다.") }
    }


}