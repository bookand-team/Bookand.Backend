package kr.co.bookand.backend.bookstore.repository;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.Theme;
import kr.co.bookand.backend.common.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookStoreRepository extends JpaRepository<BookStore, Long> {
    Optional<BookStore> findByName(String name);

    @Query(value = "SELECT b FROM BookStore b WHERE b.status = :status AND b.theme = :theme AND b.name LIKE %:name%",
    countQuery = "SELECT count (b) FROM BookStore b")
    Page<BookStore> findByStatusAndThemeAndName(
            @Param("status") Status status,
            @Param("theme") Theme theme,
            @Param("name") String name,
            Pageable pageable
    );

    Integer findByStatusAndThemeAndName(
            @Param("status") Status status,
            @Param("theme") Theme theme,
            @Param("name") String name
            );


}
