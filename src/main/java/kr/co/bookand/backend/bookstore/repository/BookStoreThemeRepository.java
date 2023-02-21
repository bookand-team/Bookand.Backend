package kr.co.bookand.backend.bookstore.repository;

import kr.co.bookand.backend.bookstore.domain.BookStoreTheme;
import kr.co.bookand.backend.bookstore.domain.BookStoreType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookStoreThemeRepository extends JpaRepository<BookStoreTheme, Long> {
}
