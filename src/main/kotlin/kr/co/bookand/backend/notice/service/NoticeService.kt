package kr.co.bookand.backend.notice.service

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.common.exception.BookandException
import kr.co.bookand.backend.common.model.MessageResponse
import kr.co.bookand.backend.notice.dto.*
import kr.co.bookand.backend.notice.model.Notice
import kr.co.bookand.backend.notice.repository.NoticeRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    ): PageResponse<NoticeResponse> {
        val nextCursorId = cursorId ?: 0L
        val page = noticeRepository.findAllByVisibilityAndStatus(pageable, true, Status.VISIBLE, nextCursorId)
            .map { NoticeResponse(it) }
        return PageResponse.of(page)
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
        return MessageResponse(result = "공지 삭제 완료", statusCode = 200)
    }

    @Transactional
    fun updateNoticeStatus(
        currentAccount: Account,
        noticeId: Long
    ): NoticeIdResponse {
        currentAccount.role.checkAdminAndManager()
        val notice = getNotice(noticeId)
        val status = notice.status
        if (status == Status.VISIBLE) notice.updateNoticeStatus(Status.INVISIBLE)
        else {
            notice.updateNoticeStatus(Status.VISIBLE)
            notice.updateDisplayAt()
        }
        return NoticeIdResponse(notice.id)
    }

    fun getNoticeDetail(noticeId: Long): NoticeResponse {
        val notice = getNotice(noticeId)
        return NoticeResponse(notice)
    }

    fun getNoticeList(
        pageable: Pageable
    ): NoticePageResponse {
        val list = noticeRepository.findAllByVisibility(pageable, true)
            .map { NoticeResponse(it) }
        return NoticePageResponse(PageResponse.of(list))
    }

    fun getNotice(noticeId: Long): Notice {
        return noticeRepository.findById(noticeId)
            .orElseThrow { throw BookandException(ErrorCode.NOT_FOUND_NOTIFICATION) }
    }


}