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

    @Query("select b from BookStore b where b.name like %:name% ")
    Page<BookStore> findAllByName(@Param("name") String name, Pageable pageable);

    @Query("select b from BookStore b where b.name like %:name% and b.theme = :theme")
    Page<BookStore> findAllByNameAndTheme(@Param("name") String name, @Param("theme") BookstoreTheme theme, Pageable pageable);

    @Query("select b from BookStore b where b.name like %:name% and b.status = :status")
    Page<BookStore> findAllByNameAndStatus(@Param("name") String name, @Param("status") Status status, Pageable pageable);

    @Query("select b from BookStore b where b.theme = :theme and b.status = :status")
    Page<BookStore> findAllByThemeAndStatus(@Param("theme") BookstoreTheme theme, @Param("status") Status status, Pageable pageable);

    @Query("select b from BookStore b where b.name like %:name% and b.theme = :theme and b.status = :status")
    Page<BookStore> findAllByNameAndThemeAndStatus(@Param("name") String name, @Param("theme") BookstoreTheme theme, @Param("status") Status status, Pageable pageable);

    Optional<BookStore> findByName(String name);

    boolean existsByName(String name);
}
