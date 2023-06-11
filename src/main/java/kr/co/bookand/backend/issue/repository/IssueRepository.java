package kr.co.bookand.backend.issue.repository;

import kr.co.bookand.backend.issue.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {

}
