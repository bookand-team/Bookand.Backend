package kr.co.bookand.backend.issue.repository

import kr.co.bookand.backend.issue.model.IssueImage
import org.springframework.data.jpa.repository.JpaRepository

interface IssueImageRepository : JpaRepository<IssueImage, Long> {
}