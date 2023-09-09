package kr.co.bookand.backend.feedback.repository

import kr.co.bookand.backend.feedback.model.Feedback
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface FeedbackRepository : JpaRepository<Feedback, Long> {

    fun countAllByVisibility(visibility: Boolean): Long

    fun countAllByVisibilityAndCreatedAtBetween(
        visibility: Boolean,
        startDatetime: LocalDateTime,
        endDatetime: LocalDateTime
    ): Long
}