package kr.co.bookand.backend.bookmark.repository;

import kr.co.bookand.backend.bookmark.BookMark;
import kr.co.bookand.backend.bookmark.BookMarkType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
    Long countBy();

    Long countByBookMarkType(BookMarkType bookMarkType);
}
