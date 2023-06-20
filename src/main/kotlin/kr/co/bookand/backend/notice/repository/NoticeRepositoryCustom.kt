package kr.co.bookand.backend.notice.repository

import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.notice.model.Notice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface NoticeRepositoryCustom {
    fun findAllByVisibilityAndStatus(
        pageable: Pageable,
        visibility: Boolean,
        status: Status,
        cursorId: Long?
    ): Page<Notice>

}