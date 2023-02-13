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
import kr.co.bookand.backend.bookmark.domain.dto.BookmarkDto;
import kr.co.bookand.backend.bookmark.exception.BookmarkException;
import kr.co.bookand.backend.bookmark.repository.BookmarkArticleRepository;
import kr.co.bookand.backend.bookmark.repository.BookmarkBookStoreRepository;
import kr.co.bookand.backend.bookmark.repository.BookmarkRepository;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.repository.BookStoreRepository;
import kr.co.bookand.backend.common.domain.Message;
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

    private static final String INIT_BOOKMARK_FOLDER_NAME = "모아보기";


    public Bookmark getMyBookmark(Account account, BookmarkType bookmarkType) {
        return bookmarkRepository.findByAccountAndFolderNameAndBookmarkType(account, INIT_BOOKMARK_FOLDER_NAME, bookmarkType)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_INIT_BOOKMARK, bookmarkType));
    }

    // 아티클 북마크 추가
    @Transactional
    public Message createArticleBookmark(Long articleId) {
        Bookmark myBookmark = getMyBookmark(getCurrentAccount(accountRepository), BookmarkType.ARTICLE);
        // 아티클이 있는지 먼저 체크
        Article bookStore = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, articleId));
        // 북마크-아티클에 추가
        BookmarkArticle bookmarkArticle = BookmarkArticle.builder()
                .bookmark(myBookmark)
                .article(bookStore)
                .build();
        bookmarkArticleRepository.save(bookmarkArticle);
        // 북마크에 추가
        myBookmark.addBookmarkArticle(bookmarkArticle);

        return Message.of("Article 추가");
    }

    // 서점 북마크 추가
    @Transactional
    public Message createBookStoreBookmark(Long bookmarkId) {
        Bookmark myBookmark = getMyBookmark(getCurrentAccount(accountRepository), BookmarkType.ARTICLE);
        // 서점이 있는지 먼저 체크
        BookStore bookStore = bookStoreRepository.findById(bookmarkId)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKSTORE, bookmarkId));
        // 북마크-서점에 추가
        BookmarkBookStore bookmarkBookStore = BookmarkBookStore.builder()
                .bookmark(myBookmark)
                .bookStore(bookStore)
                .build();
        bookmarkBookStoreRepository.save(bookmarkBookStore);
        // 북마크에 추가
        myBookmark.addBookmarkBookStore(bookmarkBookStore);

        return Message.of("BookStore 추가");
    }

    // 북마크 폴더 생성
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

        return getBookmarkResponse(bookmark);
    }

    // 북마크 폴더 내용 추가
    @Transactional
    public BookmarkResponse updateBookmarkFolder(Long bookmarkId, BookmarkContentListRequest request) {
        Account currentAccount = getCurrentAccount(accountRepository);

        Bookmark myBookmark = bookmarkRepository.findByAccountAndFolderNameAndBookmarkType(currentAccount, INIT_BOOKMARK_FOLDER_NAME, request.bookmarkType())
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_INIT_BOOKMARK, bookmarkId));

        Bookmark bookmark = bookmarkRepository.findByIdAndAccount(bookmarkId, currentAccount)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, bookmarkId));

        // 북마크 Id와 request 의 타입이 일치하는 지 확인
        if (!bookmark.getBookmarkType().equals(request.bookmarkType())) {
            throw new BookmarkException(ErrorCode.NOT_MATCH_BOOKMARK_TYPE, bookmarkId);
        }

        if (request.bookmarkType().equals(BookmarkType.BOOKSTORE)) {
            List<BookmarkBookStore> bookmarkBookStoreList = new ArrayList<>();
            request.contentIdList().forEach(contentId -> {
                // 모아보기에 있는지 체크
                bookmarkBookStoreRepository.findByBookmarkIdAndBookStoreId(myBookmark.getId(), contentId)
                        .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKSTORE, contentId));

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
        } else {
            List<BookmarkArticle> bookmarkArticleList = new ArrayList<>();
            request.contentIdList().forEach(contentId -> {
                // 모아보기에 있는지 체크
                bookmarkArticleRepository.findByBookmarkIdAndArticleId(myBookmark.getId(), contentId)
                        .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_ARTICLE, contentId));

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
        if (bookmark.getFolderName().equals(INIT_BOOKMARK_FOLDER_NAME)) {
            throw new BookmarkException(ErrorCode.NOT_CHANGE_INIT_BOOKMARK, bookmarkId);
        }
        bookmark.updateFolderName(title.folderName());
        return getBookmarkFolder(bookmarkId);
    }

    // 북마크 폴더 내용 삭제 -> 모아보기로 이동
    @Transactional
    public Message deleteBookmarkFolderContent(Long bookmarkId, BookmarkContentListRequest request) {
        Account currentAccount = getCurrentAccount(accountRepository);
        Bookmark bookmark = bookmarkRepository.findByIdAndAccount(bookmarkId, currentAccount)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, bookmarkId));
        BookmarkType bookmarkType = bookmark.getBookmarkType();
        Bookmark bookmarkCollect = bookmarkRepository.findByAccountAndFolderNameAndBookmarkType(currentAccount, INIT_BOOKMARK_FOLDER_NAME, bookmarkType)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, INIT_BOOKMARK_FOLDER_NAME));

        for (Long contentId : request.contentIdList()) {
            if (request.bookmarkType().equals(BookmarkType.BOOKSTORE)) {
                BookmarkBookStore bookmarkBookStore = bookmarkBookStoreRepository.findByBookmarkIdAndBookStoreId(bookmarkId, contentId)
                        .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK_CONTENT, contentId));
                bookmarkBookStore.updateBookmark(bookmarkCollect);
            } else {
                BookmarkArticle bookmarkArticle = bookmarkArticleRepository.findByBookmarkIdAndArticleId(bookmarkId, contentId)
                        .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK_CONTENT, contentId));
                bookmarkArticle.updateBookmark(bookmarkCollect);
            }
        }

        return Message.of("북마크 폴더 내용 삭제 성공");
    }

    // 모아보기에서 북마크 삭제
    @Transactional
    public Message deleteBookmarkContent(Long bookmarkId, BookmarkContentListRequest request) {
        Bookmark bookmark = bookmarkRepository.findByIdAndFolderNameAndBookmarkType(bookmarkId, INIT_BOOKMARK_FOLDER_NAME, request.bookmarkType())
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, bookmarkId));
        if (request.bookmarkType().equals(BookmarkType.BOOKSTORE)) {
            for (Long contentId : request.contentIdList()) {
                bookmarkBookStoreRepository.findByBookStoreIdAndBookmark(contentId, bookmark)
                        .ifPresent(
                                bookmarkBookStore -> {
                                    bookmark.removeBookmarkBookStore(bookmarkBookStore);
                                    bookmarkBookStore.getBookStore().removeBookmarkBookStore(bookmarkBookStore);
                                }
                        );
                bookmarkBookStoreRepository.deleteByBookStoreIdAndBookmarkId(contentId, bookmarkId);
                bookmark.updateBookmarkBookStore(bookmarkBookStoreRepository.findAllByBookmark(bookmark));
            }
        } else {
            for (Long contentId : request.contentIdList()) {
                bookmarkArticleRepository.findByArticleIdAndBookmark(contentId, bookmark)
                        .ifPresent(
                                bookmarkArticle -> {
                                    bookmark.removeBookmarkArticle(bookmarkArticle);
                                    bookmarkArticle.getArticle().removeBookmarkArticle(bookmarkArticle);
                                }
                        );
                bookmarkArticleRepository.deleteByArticleIdAndBookmarkId(contentId, bookmarkId);
                bookmark.updateBookmarkArticle(bookmarkArticleRepository.findAllByBookmark(bookmark));
            }
        }

        return Message.of("북마크 삭제 완료");
    }

    // 모아보기 북마크 폴더 내용 조회
    public BookmarkResponse getBookmarkCollect(String bookmarkType) {
        Account currentAccount = getCurrentAccount(accountRepository);
        BookmarkType bookmarkTypeEnum = BookmarkType.valueOf(bookmarkType.toUpperCase());
        Bookmark bookmark = bookmarkRepository.findByAccountAndFolderNameAndBookmarkType(currentAccount, INIT_BOOKMARK_FOLDER_NAME, bookmarkTypeEnum)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, bookmarkType));

        return getBookmarkResponse(bookmark);
    }

    private BookmarkResponse getBookmarkResponse(Bookmark bookmark) {
        if (bookmark.getBookmarkType().equals(BookmarkType.BOOKSTORE)) {
            List<BookmarkBookStore> bookmarkBookStoreList = bookmarkBookStoreRepository.findAllByBookmark(bookmark);
            List<BookmarkInfo> bookmarkInfoList = new ArrayList<>();
            for (BookmarkBookStore bookmarkBookStore : bookmarkBookStoreList) {
                if (bookmarkBookStore.getBookStore() != null)
                    bookmarkInfoList.add(BookmarkInfo.ofBookStore(bookmarkBookStore.getBookStore()));
            }
            Page<BookmarkInfo> bookmarkInfo = new PageImpl<>(bookmarkInfoList);
            return BookmarkResponse.of(bookmark, bookmarkInfo);
        } else {
            List<BookmarkArticle> bookmarkArticleList = bookmarkArticleRepository.findAllByBookmark(bookmark);
            List<BookmarkInfo> bookmarkInfoList = new ArrayList<>();
            for (BookmarkArticle bookmarkArticle : bookmarkArticleList) {
                if (bookmarkArticle.getArticle() != null)
                    bookmarkInfoList.add(BookmarkInfo.ofArticle(bookmarkArticle.getArticle()));
            }
            Page<BookmarkInfo> bookmarkInfo = new PageImpl<>(bookmarkInfoList);
            return BookmarkResponse.of(bookmark, bookmarkInfo);
        }
    }
}
