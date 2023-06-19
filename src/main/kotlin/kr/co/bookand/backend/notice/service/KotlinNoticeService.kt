package kr.co.bookand.backend.notice.service

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.common.KotlinPageResponse
import kr.co.bookand.backend.common.KotlinStatus
import kr.co.bookand.backend.common.domain.KotlinMessageResponse
import kr.co.bookand.backend.notice.domain.KotlinNotice
import kr.co.bookand.backend.notice.domain.dto.*
import kr.co.bookand.backend.notice.repository.KotlinNoticeRepository
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.Pageable
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
    fun createNotice(
        currentAccount: KotlinAccount,
        createNoticeRequest: KotlinCreateNoticeRequest
    ): KotlinNoticeIdResponse {
        currentAccount.role.checkAdminAndManager()
        val kotlinNotice = KotlinNotice(createNoticeRequest)
        val saveNotice = noticeRepository.save(kotlinNotice)
        return KotlinNoticeIdResponse(saveNotice.id)
    }

    fun getNoticeSimpleList(
        pageable: Pageable?,
        cursorId: Long?
    ): KotlinNoticeListResponse {
        val nextCursorId = cursorId ?: 0L
        val page = noticeRepository.findAllByVisibilityAndStatus(pageable, true, KotlinStatus.VISIBLE, nextCursorId)
            .map { KotlinNoticeResponse(it) }
        val totalElements = noticeRepository.count()
        val ofCursor = KotlinPageResponse.ofCursor(page, totalElements)
        return KotlinNoticeListResponse(ofCursor)
    }

    @Transactional
    fun updateNotice(
        currentAccount: KotlinAccount,
        noticeId: Long,
        updateNoticeRequest: KotlinUpdateNoticeRequest
    ): KotlinNoticeIdResponse {
        currentAccount.role.checkAdminAndManager()
        val kotlinNotice = getNotice(noticeId)
        kotlinNotice.updateNoticeData(updateNoticeRequest)
        return KotlinNoticeIdResponse(kotlinNotice.id)
    }

    @Transactional
    fun deleteNotice(currentAccount: KotlinAccount, noticeId: Long): KotlinMessageResponse {
        currentAccount.role.checkAdminAndManager()
        noticeRepository.deleteById(noticeId)
        return KotlinMessageResponse(message = "Success", statusCode = 200)
    }

    @Transactional
    fun updateNoticeStatus(
        currentAccount: KotlinAccount,
        noticeId: Long
    ): KotlinMessageResponse {
        currentAccount.role.checkAdminAndManager()
        val kotlinNotice = getNotice(noticeId)
        return if (kotlinNotice.status == KotlinStatus.INVISIBLE) {
            kotlinNotice.status = KotlinStatus.VISIBLE
            KotlinMessageResponse(message = "Update Status to Visible.", statusCode = 200)
        } else {
            kotlinNotice.status = KotlinStatus.INVISIBLE
            KotlinMessageResponse(message = "Update Status to Invisible.", statusCode = 200)
        }
    }

    fun getNoticeDetail(noticeId: Long): KotlinNoticeResponse {
        val kotlinNotice = getNotice(noticeId)
        return KotlinNoticeResponse(kotlinNotice)
    }

    fun getNoticeList(
        pageable: Pageable?
    ): KotlinNoticeListResponse {
        val list = noticeRepository.findAllByStatusAndVisibility(pageable, KotlinStatus.VISIBLE, true)
            .map { KotlinNoticeResponse(it) }
        return KotlinNoticeListResponse(KotlinPageResponse.of(list))
    }

    fun getNotice(noticeId: Long): KotlinNotice {
        return noticeRepository.findById(noticeId).orElseThrow { throw Exception("존재하지 않는 공지사항입니다.") }
    }


}