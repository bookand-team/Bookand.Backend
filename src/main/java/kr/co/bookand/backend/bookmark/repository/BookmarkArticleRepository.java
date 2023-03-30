package kr.co.bookand.backend.bookmark.repository;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.bookmark.domain.Bookmark;
import kr.co.bookand.backend.bookmark.domain.BookmarkArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkArticleRepository extends JpaRepository<BookmarkArticle, Long>, BookmarkArticleRepositoryCustom {

    List<BookmarkArticle> findAllByBookmark(Bookmark bookmark);

    void deleteByArticleIdAndBookmarkId(Long contentId, Long bookmarkId);

    Optional<BookmarkArticle> findByBookmarkIdAndArticleId(Long bookmarkId, Long contentId);

    void deleteByBookmarkIdAndArticleId(Long bookmarkId, Long contentId);

    Optional<BookmarkArticle> findByArticleIdAndBookmark(Long contentId, Bookmark bookmark);

    Optional<BookmarkArticle> findFirstByBookmark(Bookmark bookmark);

    Optional<BookmarkArticle> findFirstByBookmarkId(Long bookmarkId);

    Long countAllByBookmark(Bookmark bookmark);

    Optional<BookmarkArticle> findByArticleId(Long articleId);

}
