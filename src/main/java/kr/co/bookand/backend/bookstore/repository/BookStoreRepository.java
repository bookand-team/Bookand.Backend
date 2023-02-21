package kr.co.bookand.backend.bookstore.repository;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.BookStoreType;
import kr.co.bookand.backend.common.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookStoreRepository extends JpaRepository<BookStore, Long> {

    Page<BookStore> findAllByThemeList(BookStoreType themeList, Pageable pageable);

    Page<BookStore> findAllByStatus(Status status, Pageable pageable);

    Page<BookStore> findAllByNameContaining(String name, Pageable pageable);

    Page<BookStore> findAllByNameContainingAndThemeList(String name, BookStoreType themeList, Pageable pageable);

    Page<BookStore> findAllByThemeListContainsAndStatus(BookStoreType themeList, Status status, Pageable pageable);

    Page<BookStore> findAllByNameContainingAndStatus(String name, Status status, Pageable pageable);

    Page<BookStore> findAllByNameContainingAndThemeListContainingAndStatus(String name, BookStoreType themeList, Status status, Pageable pageable);

    boolean existsByName(String name);
}
