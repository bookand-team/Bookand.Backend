package kr.co.bookand.backend.bookmark.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.exception.ArticleException;
import kr.co.bookand.backend.article.repository.ArticleRepository;
import kr.co.bookand.backend.bookmark.domain.Bookmark;
import kr.co.bookand.backend.bookmark.domain.BookmarkArticle;
import kr.co.bookand.backend.bookmark.domain.BookmarkBookStore;
import kr.co.bookand.backend.bookmark.domain.BookmarkType;
import kr.co.bookand.backend.bookmark.exception.BookmarkException;
import kr.co.bookand.backend.bookmark.repository.BookmarkArticleRepository;
import kr.co.bookand.backend.bookmark.repository.BookmarkBookStoreRepository;
import kr.co.bookand.backend.bookmark.repository.BookmarkRepository;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.repository.BookStoreRepository;
import kr.co.bookand.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.bookand.backend.bookmark.domain.dto.BookmarkDto.*;
import static kr.co.bookand.backend.config.security.SecurityUtils.getCurrentAccount;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkArticleRepository bookmarkArticleRepository;
    private final BookmarkBookStoreRepository bookmarkBookStoreRepository;
    private final AccountRepository accountRepository;
    private final BookStoreRepository bookStoreRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public BookmarkResponse createBookmarkFolder(BookmarkRequest bookmarkRequest) {
        Account currentAccount = getCurrentAccount(accountRepository);
        Bookmark bookmark = bookmarkRequest.toEntity(currentAccount);
        bookmarkRepository.save(bookmark);
        if (bookmark.getBookmarkType().equals(BookmarkType.BOOKSTORE)) {
            BookmarkBookStore bookStore = BookmarkBookStore.builder()
                    .bookmark(bookmark)
                    .bookStore(null)
                    .build();
            bookmarkBookStoreRepository.save(bookStore);
        } else {
            BookmarkArticle bookmarkArticle = BookmarkArticle.builder()
                    .bookmark(bookmark)
                    .article(null)
                    .build();
            bookmarkArticleRepository.save(bookmarkArticle);
        }
        Page<BookmarkInfo> bookmarkInfo = new PageImpl<>(List.of());
        return BookmarkResponse.of(bookmark, bookmarkInfo);
    }

    // 모든 북마크 폴더 리스트 확인
    public BookmarkFolderListResponse getBookmarkFolderList(String bookmarkType) {
        log.info("getBookmarkFolderList {} ", bookmarkType);
        Account currentAccount = getCurrentAccount(accountRepository);
        BookmarkType type = BookmarkType.valueOf(bookmarkType.toUpperCase());
        log.info("type : {}", type);
        List<Bookmark> bookmarkList = bookmarkRepository.findAllByAccountAndBookmarkType(currentAccount, type);
        List<BookmarkFolderResponse> bookmarkFolderList = bookmarkList.stream().map(BookmarkFolderResponse::of)
                .collect(Collectors.toList());
        return new BookmarkFolderListResponse(bookmarkFolderList);
    }

    // 북마크 폴더 내용 조회
    public BookmarkResponse getBookmarkFolder(Long bookmarkId) {
        Account currentAccount = getCurrentAccount(accountRepository);
        Bookmark bookmark = bookmarkRepository.findByIdAndAccount(bookmarkId, currentAccount)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, bookmarkId));

        if (bookmark.getBookmarkType().equals(BookmarkType.BOOKSTORE)) {
            List<BookmarkBookStore> bookmarkBookStoreList = bookmarkBookStoreRepository.findAllByBookmark(bookmark);
            List<BookmarkInfo> bookmarkInfoList = new ArrayList<>();
            for (BookmarkBookStore bookmarkBookStore : bookmarkBookStoreList) {
                if(bookmarkBookStore.getBookStore() != null) {
                    bookmarkInfoList.add(BookmarkInfo.ofBookStore(bookmarkBookStore.getBookStore()));
                }
            }
            Page<BookmarkInfo> bookmarkInfo = new PageImpl<>(bookmarkInfoList);
            return BookmarkResponse.of(bookmark, bookmarkInfo);
        } else {
            List<BookmarkArticle> bookmarkArticleList = bookmarkArticleRepository.findAllByBookmark(bookmark);
            List<BookmarkInfo> bookmarkInfoList = new ArrayList<>();
            for (BookmarkArticle bookmarkArticle : bookmarkArticleList) {
                bookmarkInfoList.add(BookmarkInfo.ofArticle(bookmarkArticle.getArticle()));
            }
            Page<BookmarkInfo> bookmarkInfo = new PageImpl<>(bookmarkInfoList);
            return BookmarkResponse.of(bookmark, bookmarkInfo);
        }
    }

    // 북마크 폴더 내용 추가
    @Transactional
    public BookmarkResponse updateBookmarkFolder(Long bookmarkId, BookmarkAddContentRequest request) {
        Account currentAccount = getCurrentAccount(accountRepository);
        Bookmark bookmark = bookmarkRepository.findByIdAndAccount(bookmarkId, currentAccount)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, bookmarkId));

        if (request.bookmarkType().equals(BookmarkType.BOOKSTORE)) {
            List<BookmarkBookStore> bookmarkBookStoreList = new ArrayList<>();
            request.contentIdList().forEach(contentId -> {
                // 서점이 있는지 먼저 체크
                BookStore bookStore = bookStoreRepository.findById(contentId)
                        .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKSTORE, contentId));
                // 북마크-서점에 추가
                BookmarkBookStore bookmarkBookStore = BookmarkBookStore.builder()
                        .bookmark(bookmark)
                        .bookStore(bookStore)
                        .build();
                bookmarkBookStoreRepository.save(bookmarkBookStore);
                // 북마크에 추가
                bookmarkBookStoreList.add(bookmarkBookStore);
            });
            bookmark.updateBookmarkBookStore(bookmarkBookStoreList);
        }else {
            List<BookmarkArticle> bookmarkArticleList = new ArrayList<>();
            request.contentIdList().forEach(contentId -> {
                // 아티클이 있는지 먼저 체크
                Article bookStore = articleRepository.findById(contentId)
                        .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, contentId));
                // 북마크-아티클에 추가
                BookmarkArticle bookmarkBookStore = BookmarkArticle.builder()
                        .bookmark(bookmark)
                        .article(bookStore)
                        .build();
                bookmarkArticleRepository.save(bookmarkBookStore);
                // 북마크에 추가
                bookmarkArticleList.add(bookmarkBookStore);
            });
            bookmark.updateBookmarkArticle(bookmarkArticleList);
        }

        return getBookmarkFolder(bookmarkId);
    }


    // 북마크 이름 변경
    @Transactional
    public BookmarkResponse updateBookmarkFolderName(Long bookmarkId, BookmarkFolderNameRequest title) {
        Account currentAccount = getCurrentAccount(accountRepository);
        Bookmark bookmark = bookmarkRepository.findByIdAndAccount(bookmarkId, currentAccount)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, bookmarkId));
        bookmark.updateFolderName(title.folderName());
        return getBookmarkFolder(bookmarkId);
    }

}
