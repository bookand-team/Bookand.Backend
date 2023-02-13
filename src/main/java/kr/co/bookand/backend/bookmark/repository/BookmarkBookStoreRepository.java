package kr.co.bookand.backend.bookmark.repository;

import kr.co.bookand.backend.bookmark.domain.Bookmark;
import kr.co.bookand.backend.bookmark.domain.BookmarkBookStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkBookStoreRepository extends JpaRepository<BookmarkBookStore, Long> {

    List<BookmarkBookStore> findAllByBookmark(Bookmark bookmark);

    void deleteByBookStoreIdAndBookmarkId(Long id, Long bookmarkId);

    Optional<BookmarkBookStore> findByBookmarkIdAndBookStoreId(Long bookmarkId, Long contentId);

    Optional<BookmarkBookStore> findByBookStoreIdAndBookmark(Long contentId, Bookmark bookmark);
}
