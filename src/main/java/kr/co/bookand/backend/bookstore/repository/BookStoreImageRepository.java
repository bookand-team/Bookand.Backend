package kr.co.bookand.backend.bookstore.repository;

import kr.co.bookand.backend.bookstore.domain.BookStoreImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookStoreImageRepository extends JpaRepository<BookStoreImage, Long> {
}

