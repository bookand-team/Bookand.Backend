package kr.co.bookand.backend.bookmark.repository;

import kr.co.bookand.backend.bookmark.domain.Bookmark;
import kr.co.bookand.backend.bookmark.domain.BookmarkBookStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkBookStoreRepository extends JpaRepository<BookmarkBookStore, Long> {

    List<BookmarkBookStore> findAllByBookmark(Bookmark bookmark);
}
