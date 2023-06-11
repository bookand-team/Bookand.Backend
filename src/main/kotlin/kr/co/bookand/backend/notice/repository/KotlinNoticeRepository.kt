package kr.co.bookand.backend.notice.repository

import kr.co.bookand.backend.notice.domain.KotlinNotice
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinNoticeRepository : JpaRepository<KotlinNotice, Long> {
}