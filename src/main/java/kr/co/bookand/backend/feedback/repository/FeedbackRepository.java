package kr.co.bookand.backend.feedback.repository;

import kr.co.bookand.backend.feedback.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}