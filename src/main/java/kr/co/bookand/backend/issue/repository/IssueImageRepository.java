package kr.co.bookand.backend.issue.repository;

import kr.co.bookand.backend.issue.domain.IssueImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueImageRepository extends JpaRepository<IssueImage, Long> {
}
