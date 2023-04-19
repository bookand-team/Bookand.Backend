package kr.co.bookand.backend.bookstore.repository;

import kr.co.bookand.backend.bookstore.domain.ReportBookStore;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportBookStoreRepository extends JpaRepository<ReportBookStore, Long> {
    Long countAllByVisibilityAndIsAnswered(boolean visibility, boolean isAnswered);

    Long countAllByVisibilityAndCreatedAtBetween(boolean visibility, String startDatetime, String endDatetime);

    List<ReportBookStore> findAllByName(String name);

    @Query("SELECT r " +
            "FROM ReportBookStore r " +
            "WHERE r.id = ( " +
            "   SELECT MIN(r2.id) " +
            "   FROM ReportBookStore r2 " +
            "   WHERE r2.name = r.name" +
            ")")
    Page<ReportBookStore> findAllByName(Pageable pageable);

    Long countAllByName(String name);
}

