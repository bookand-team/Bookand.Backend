package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.bookstore.domain.ReportBookstore
import org.springframework.data.jpa.repository.JpaRepository

interface ReportBookstoreRepository : JpaRepository<ReportBookstore, Long> {
    fun countAllByVisibilityAndIsAnswered(visibility: Boolean, isAnswered: Boolean): Long

    fun countAllByVisibilityAndCreatedAtBetween(
        visibility: Boolean,
        startDatetime: String,
        endDatetime: String
    ): Long
}