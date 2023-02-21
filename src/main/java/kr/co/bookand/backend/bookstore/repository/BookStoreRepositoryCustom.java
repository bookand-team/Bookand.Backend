package kr.co.bookand.backend.bookstore.repository;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.common.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookStoreRepositoryCustom {
    Page<BookStore> findAllBySearch(String search, String theme, String status, Pageable pageable);
}
