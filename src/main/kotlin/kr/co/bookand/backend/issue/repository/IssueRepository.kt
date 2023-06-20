package kr.co.bookand.backend.issue.repository

import kr.co.bookand.backend.issue.model.Issue
import org.springframework.data.jpa.repository.JpaRepository

interface IssueRepository : JpaRepository<Issue, Long> {
}