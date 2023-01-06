package kr.co.bookand.backend.bookstore.repository;

import kr.co.bookand.backend.bookstore.domain.ReportBookStore;
import kr.co.bookand.backend.common.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ReportBookStoreRepository extends JpaRepository<ReportBookStore, Long> {
    @Query(value = "SELECT COUNT(b) from ReportBookStore b WHERE b.createdAt >= ?1 AND b.createdAt <= ?1")
    Long countByCreatedAtBetween(String start, String end);

    Long countByStatus(Status status);
}

