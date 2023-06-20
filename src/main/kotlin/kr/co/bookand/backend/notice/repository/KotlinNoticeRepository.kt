package kr.co.bookand.backend.notice.repository

import kr.co.bookand.backend.common.KotlinStatus
import kr.co.bookand.backend.notice.domain.KotlinNotice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinNoticeRepository : JpaRepository<KotlinNotice, Long>, KotlinNoticeRepositoryCustom {
    fun findAllByStatusAndVisibility(
        pageable: Pageable,
        status: KotlinStatus,
        visibility: Boolean
    ): Page<KotlinNotice>
}