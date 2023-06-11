package kr.co.bookand.backend.issue.repository

import kr.co.bookand.backend.issue.domain.KotlinIssueImage
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinIssueImageRepository : JpaRepository<KotlinIssueImage, Long> {
}