package kr.co.bookand.backend.bookmark.repository;

import kr.co.bookand.backend.bookmark.domain.Bookmark;
import kr.co.bookand.backend.bookmark.domain.BookmarkArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkArticleRepository extends JpaRepository<BookmarkArticle, Long> {

    List<BookmarkArticle> findAllByBookmark(Bookmark bookmark);
}
