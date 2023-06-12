package kr.co.bookand.backend.feedback.repository

import kr.co.bookand.backend.feedback.domain.KotlinFeedback
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinFeedbackRepository : JpaRepository<KotlinFeedback, Long> {
}