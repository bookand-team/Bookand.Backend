package kr.co.bookand.backend.bookstore.repository;

import kr.co.bookand.backend.bookstore.domain.ReportBookStore;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportBookStoreRepository extends JpaRepository<ReportBookStore, Long> {
}
