package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.bookstore.model.ReportBookstore
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ReportBookstoreRepository : JpaRepository<ReportBookstore, Long> {
    fun countAllByVisibilityAndCheckAnswered(visibility: Boolean, checkAnswered: Boolean): Long

    fun countAllByVisibilityAndCreatedAtBetween(
        visibility: Boolean,
        startDatetime: LocalDateTime,
        endDatetime: LocalDateTime
    ): Long
}