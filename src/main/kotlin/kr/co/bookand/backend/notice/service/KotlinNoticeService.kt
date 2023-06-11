package kr.co.bookand.backend.notice.service

import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.common.domain.Status
import kr.co.bookand.backend.notice.domain.KotlinNotice
import kr.co.bookand.backend.notice.domain.dto.CreateNoticeRequest
import kr.co.bookand.backend.notice.domain.dto.NoticeIdResponse
import kr.co.bookand.backend.notice.domain.dto.NoticeMessageResponse
import kr.co.bookand.backend.notice.domain.dto.UpdateNoticeRequest
import kr.co.bookand.backend.notice.repository.KotlinNoticeRepository
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
class KotlinNoticeService(
    private val noticeRepository: KotlinNoticeRepository,
    private val accountService: KotlinAccountService
) {

    @Transactional
    fun createNotice(accountId: Long, createNoticeRequest: CreateNoticeRequest): NoticeIdResponse {
        accountService.checkAccountAdmin(accountId)
        val kotlinNotice = KotlinNotice(createNoticeRequest)
        val saveNotice = noticeRepository.save(kotlinNotice)
        return NoticeIdResponse(saveNotice.id)
    }

    @Transactional
    fun updateNotice(accountId: Long, noticeId: Long, updateNoticeRequest: UpdateNoticeRequest): NoticeIdResponse {
        accountService.checkAccountAdmin(accountId)
        val kotlinNotice = getNotice(noticeId)
        kotlinNotice.updateNoticeData(updateNoticeRequest)
        return NoticeIdResponse(kotlinNotice.id)
    }

    @Transactional
    fun deleteNotice(accountId: Long, noticeId: Long): NoticeIdResponse {
        accountService.checkAccountAdmin(accountId)
        noticeRepository.deleteById(noticeId)
        return NoticeIdResponse(noticeId)
    }

    @Transactional
    fun updateNoticeStatus(accountId: Long, noticeId: Long, status: Status): NoticeMessageResponse {
        accountService.checkAccountAdmin(accountId)
        val kotlinNotice = getNotice(noticeId)
        return if (kotlinNotice.status == Status.INVISIBLE) {
            NoticeMessageResponse("Update Status to Visible.")
        } else NoticeMessageResponse("Update Status to Invisible.")
    }

    fun getNotice(noticeId: Long): KotlinNotice {
        return noticeRepository.findById(noticeId).orElseThrow { throw Exception("존재하지 않는 공지사항입니다.") }
    }


}