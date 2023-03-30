package kr.co.bookand.backend.bookmark.repository;

import kr.co.bookand.backend.bookmark.domain.Bookmark;
import kr.co.bookand.backend.bookmark.domain.BookmarkBookStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkBookStoreRepositoryCustom {

    Page<BookmarkBookStore> findAllByBookmarkAndAndVisibilityTrue(
            Bookmark bookmark,
            Pageable pageable,
            Long cursorId,
            String createdAt
    );


}
