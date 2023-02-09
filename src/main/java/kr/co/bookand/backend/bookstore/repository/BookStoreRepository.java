package kr.co.bookand.backend.bookstore.repository;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.BookstoreTheme;
import kr.co.bookand.backend.common.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookStoreRepository extends JpaRepository<BookStore, Long> {

    Page<BookStore> findAllByTheme(BookstoreTheme theme, Pageable pageable);

    Page<BookStore> findAllByStatus(Status status, Pageable pageable);

    Page<BookStore> findAllByNameContaining(String name, Pageable pageable);

    Page<BookStore> findAllByNameContainingAndTheme(String name, BookstoreTheme theme, Pageable pageable);

    Page<BookStore> findAllByNameContainingAndStatus(String name, Status status, Pageable pageable);

    Page<BookStore> findAllByThemeAndStatus( BookstoreTheme theme,  Status status, Pageable pageable);

    Page<BookStore> findAllByNameContainingAndThemeAndStatus(String name,  BookstoreTheme theme,  Status status, Pageable pageable);

    boolean existsByName(String name);
}
