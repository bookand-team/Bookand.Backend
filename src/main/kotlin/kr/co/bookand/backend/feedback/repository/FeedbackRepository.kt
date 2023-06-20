package kr.co.bookand.backend.feedback.repository

import kr.co.bookand.backend.feedback.domain.Feedback
import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackRepository : JpaRepository<Feedback, Long> {

    fun countAllByVisibility(visibility: Boolean): Long

    fun countAllByVisibilityAndCreatedAtBetween(
        visibility: Boolean,
        startDatetime: String,
        endDatetime: String
    ): Long
}