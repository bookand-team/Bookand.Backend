package kr.co.bookand.backend.notice.repository

import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.notice.domain.Notice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface NoticeRepository : JpaRepository<Notice, Long>, NoticeRepositoryCustom {
    fun findAllByStatusAndVisibility(
        pageable: Pageable,
        status: Status,
        visibility: Boolean
    ): Page<Notice>
}