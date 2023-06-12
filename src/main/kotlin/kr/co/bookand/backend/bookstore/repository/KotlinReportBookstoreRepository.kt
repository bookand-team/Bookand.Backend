package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.bookstore.domain.KotlinReportBookstore
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinReportBookstoreRepository : JpaRepository<KotlinReportBookstore, Long> {
    fun countAllByVisibilityAndIsAnswered(visibility: Boolean, isAnswered: Boolean): Long

    fun countAllByVisibilityAndCreatedAtBetween(
        visibility: Boolean,
        startDatetime: String,
        endDatetime: String
    ): Long
}