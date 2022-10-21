package kr.co.bookand.backend.bookstore.repository;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookStoreRepository extends JpaRepository<BookStore, Long> {
    Optional<BookStore> findByName(String name);
}
