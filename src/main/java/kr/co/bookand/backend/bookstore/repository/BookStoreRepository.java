package kr.co.bookand.backend.bookstore.repository;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.common.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookStoreRepository extends JpaRepository<BookStore, Long>, BookStoreRepositoryCustom {

    Page<BookStore> findAllByStatus(Status status, Pageable pageable);

    boolean existsByName(String name);

    Long countAllByVisibility(boolean visibility);

    Long countAllByVisibilityAndStatus(boolean visibility, Status status);
}
