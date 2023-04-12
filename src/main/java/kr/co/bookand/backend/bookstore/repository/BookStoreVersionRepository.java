package kr.co.bookand.backend.bookstore.repository;

import kr.co.bookand.backend.bookstore.domain.BookStoreVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookStoreVersionRepository extends JpaRepository<BookStoreVersion, Long> {
    BookStoreVersion findFirstByOrderByIdDesc();
}
