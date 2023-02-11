package kr.co.bookand.backend.bookmark.repository;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.bookmark.domain.Bookmark;
import kr.co.bookand.backend.bookmark.domain.BookmarkType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findAllByAccountAndBookmarkType(Account account, BookmarkType bookmarkType);

    Optional<Bookmark> findByIdAndAccount(Long bookmarkId, Account currentAccount);
}
