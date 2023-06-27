package kr.co.bookand.backend.notice.repository

import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.notice.model.Notice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface NoticeRepository : JpaRepository<Notice, Long>, NoticeRepositoryCustom {
    fun findAllByVisibility(
        pageable: Pageable,
        visibility: Boolean
    ): Page<Notice>
}