package kr.co.bookand.backend.notice.repository

import kr.co.bookand.backend.common.KotlinStatus
import kr.co.bookand.backend.notice.domain.KotlinNotice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface KotlinNoticeRepositoryCustom {
    fun findAllByVisibilityAndStatus(
        pageable: Pageable?,
        visibility: Boolean,
        status: KotlinStatus,
        cursorId: Long?
    ): Page<KotlinNotice>

}