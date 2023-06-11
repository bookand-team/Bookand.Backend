package kr.co.bookand.backend.issue.repository

import kr.co.bookand.backend.issue.domain.KotlinIssue
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinIssueRepository : JpaRepository<KotlinIssue, Long> {
}