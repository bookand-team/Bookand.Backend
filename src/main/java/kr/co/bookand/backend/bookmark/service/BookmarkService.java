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
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto;
import kr.co.bookand.backend.bookstore.exception.BookStoreException;
import kr.co.bookand.backend.bookstore.repository.BookStoreRepository;
import kr.co.bookand.backend.common.domain.Message;
import kr.co.bookand.backend.common.domain.Status;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import kr.co.bookand.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public BookmarkBookStore getBookmarkBookStore(Long bookmarkBookStoreId) {
        return bookmarkBookStoreRepository.findByBookStoreId(bookmarkBookStoreId)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK_BOOKSTORE, bookmarkBookStoreId));
    }

    public BookmarkArticle getBookmarkArticle(Long bookmarkArticleId) {
        return bookmarkArticleRepository.findByArticleId(bookmarkArticleId)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK_ARTICLE, bookmarkArticleId));
    }

    // 아티클 북마크 추가
    @Transactional
    public Message createArticleBookmark(Long articleId) {
        Bookmark myBookmark = getMyBookmark(getCurrentAccount(accountRepository), BookmarkType.ARTICLE);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, articleId));

        boolean isBookmarked = bookmarkArticleRepository
                .findByBookmarkIdAndArticleId(myBookmark.getId(), article.getId())
                .isPresent();

        if (isBookmarked) {
            deleteBookmarkArticle(myBookmark, article);
            return Message.of("북마크 삭제");
        } else {
            createBookmarkArticle(myBookmark, article);
            return Message.of("북마크 추가");
        }
    }

    private void createBookmarkArticle(Bookmark myBookmark, Article article) {
        BookmarkArticle bookmarkArticle = BookmarkArticle.builder()
                .bookmark(myBookmark)
                .article(article)
                .build();
        myBookmark.addBookmarkArticle(bookmarkArticle);
    }

    private void deleteBookmarkArticle(Bookmark myBookmark, Article article) {
        bookmarkArticleRepository.deleteByArticleIdAndBookmarkId(article.getId(), myBookmark.getId());
    }

    // 서점 북마크 추가
    @Transactional
    public Message createBookStoreBookmark(Long bookmarkId) {
        Bookmark myBookmark = getMyBookmark(getCurrentAccount(accountRepository), BookmarkType.BOOKSTORE);
        // 서점이 있는지 먼저 체크
        BookStore bookStore = bookStoreRepository.findById(bookmarkId)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKSTORE, bookmarkId));

        // 이미 북마크에 있는지 확인
        Optional<BookmarkBookStore> checkBookmark = bookmarkBookStoreRepository
                .findByBookmarkIdAndBookStoreId(myBookmark.getId(), bookStore.getId());

        if (checkBookmark.isEmpty()) {
            // 북마크-서점에 추가
            BookmarkBookStore bookmarkBookStore = BookmarkBookStore.builder()
                    .bookmark(myBookmark)
                    .bookStore(bookStore)
                    .build();
            bookmarkBookStoreRepository.save(bookmarkBookStore);
            // 북마크에 추가
            myBookmark.addBookmarkBookStore(bookmarkBookStore);

            return Message.of("북마크 추가");
        } else {
            // 북마크 취소
            bookmarkBookStoreRepository.deleteByBookStoreIdAndBookmarkId(bookmarkId, myBookmark.getId());
            return Message.of("북마크 삭제");
        }
    }

    // 북마크 폴더 생성
    @Transactional
    public BookmarkResponseId createBookmarkFolder(BookmarkRequest bookmarkRequest) {
        Account currentAccount = getCurrentAccount(accountRepository);
        Bookmark bookmark = bookmarkRequest.toEntity(currentAccount);
        bookmarkRepository.save(bookmark);
        return BookmarkResponseId.of(bookmark);
    }

    // 모든 북마크 폴더 리스트 확인
    public BookmarkFolderListResponse getBookmarkFolderList(String bookmarkType) {
        Account currentAccount = getCurrentAccount(accountRepository);
        BookmarkType type = BookmarkType.valueOf(bookmarkType.toUpperCase());
        List<Bookmark> bookmarkList = bookmarkRepository.findAllByAccountAndBookmarkType(currentAccount, type);
        List<BookmarkFolderResponse> bookmarkFolderList = bookmarkList.stream().map(BookmarkFolderResponse::of)
                .toList();
        return new BookmarkFolderListResponse(bookmarkFolderList);
    }

    // 북마크 폴더 내용 조회
    public BookmarkResponse getBookmarkFolder(Long bookmarkId, Pageable pageable, Long cursorId) {
        Account currentAccount = getCurrentAccount(accountRepository);
        Bookmark bookmark = bookmarkRepository.findByIdAndAccount(bookmarkId, currentAccount)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, bookmarkId));
        return getBookmarkResponse(bookmark, pageable, cursorId);
    }

    // 북마크 폴더 내용 추가
    @Transactional
    public BookmarkResponseId updateBookmarkFolder(Long bookmarkId, BookmarkContentListRequest request) {
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
                // 기존 정보에 있는지 확인
                bookmarkBookStoreRepository.findByBookmarkIdAndBookStoreId(bookmark.getId(), contentId)
                        .ifPresentOrElse(bookmarkArticle -> {
                        }, () -> {
                            // 서점이 있는지 먼저 체크
                            BookStore bookStore = bookStoreRepository.findById(contentId)
                                    .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKSTORE, contentId));
                            // 북마크-서점에 추가
                            BookmarkBookStore bookmarkBookStore = BookmarkBookStore.builder()
                                    .bookmark(bookmark)
                                    .bookStore(bookStore)
                                    .build();
                            bookmarkBookStoreRepository.save(bookmarkBookStore);
                            bookmark.updateFolderImage(bookStore.getMainImage());
                            // 북마크에 추가
                            bookmarkBookStoreList.add(bookmarkBookStore);
                        });
            });
            bookmark.updateBookmarkBookStore(bookmarkBookStoreList);
        } else {
            List<BookmarkArticle> bookmarkArticleList = new ArrayList<>();
            request.contentIdList().forEach(contentId -> {
                // 모아보기에 있는지 체크
                bookmarkArticleRepository.findByBookmarkIdAndArticleId(myBookmark.getId(), contentId)
                        .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_ARTICLE, contentId));
                // 기존 정보에 있는지 확인
                bookmarkArticleRepository.findByBookmarkIdAndArticleId(bookmark.getId(), contentId)
                        .ifPresentOrElse(bookmarkArticle -> {
                        }, () -> {
                            // 아티클이 있는지 먼저 체크
                            Article article = articleRepository.findById(contentId)
                                    .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, contentId));
                            // 북마크-아티클에 추가
                            BookmarkArticle bookmarkBookStore = BookmarkArticle.builder()
                                    .bookmark(bookmark)
                                    .article(article)
                                    .build();
                            bookmarkArticleRepository.save(bookmarkBookStore);
                            bookmark.updateFolderImage(article.getMainImage());
                            // 북마크에 추가
                            bookmarkArticleList.add(bookmarkBookStore);
                        });

            });
            bookmark.updateBookmarkArticle(bookmarkArticleList);
        }

        return BookmarkResponseId.of(bookmark);
    }

    // 북마크 이름 변경
    @Transactional
    public BookmarkResponseId updateBookmarkFolderName(Long bookmarkId, BookmarkFolderNameRequest title) {
        Account currentAccount = getCurrentAccount(accountRepository);
        Bookmark bookmark = bookmarkRepository.findByIdAndAccount(bookmarkId, currentAccount)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, bookmarkId));
        if (bookmark.getFolderName().equals(INIT_BOOKMARK_FOLDER_NAME)) {
            throw new BookmarkException(ErrorCode.NOT_CHANGE_INIT_BOOKMARK, bookmarkId);
        }
        bookmark.updateFolderName(title.folderName());
        return BookmarkResponseId.of(bookmark);
    }

    // 북마크 폴더 내용 삭제 -> 모아보기로 이동
    @Transactional
    public Message deleteBookmarkFolderContent(Long bookmarkId, BookmarkContentListRequest request) {
        Account currentAccount = getCurrentAccount(accountRepository);
        Bookmark bookmark = bookmarkRepository.findByIdAndAccount(bookmarkId, currentAccount)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, bookmarkId));

        for (Long contentId : request.contentIdList()) {
            if (request.bookmarkType().equals(BookmarkType.BOOKSTORE)) {
                bookmarkBookStoreRepository.deleteByBookmarkIdAndBookStoreId(bookmarkId, contentId);
            } else {
                bookmarkArticleRepository.deleteByBookmarkIdAndArticleId(bookmarkId, contentId);
            }
        }
        bookmarkRepository.flush();

        // 최근 이미지 설정
        if (request.bookmarkType().equals(BookmarkType.BOOKSTORE)) {
            Optional<BookmarkBookStore> firstByBookmark = bookmarkBookStoreRepository.findFirstByBookmark(bookmark);
            if (firstByBookmark.isEmpty()) {
                bookmark.updateFolderImage(null);
            } else {
                bookmark.updateFolderImage(firstByBookmark.get().getBookStore().getMainImage());
            }
        } else {
            Optional<BookmarkArticle> firstByBookmark = bookmarkArticleRepository.findFirstByBookmark(bookmark);
            if (firstByBookmark.isEmpty()) {
                bookmark.updateFolderImage(null);
            } else {
                bookmark.updateFolderImage(firstByBookmark.get().getArticle().getMainImage());
            }
        }
        return Message.of("북마크 폴더 내용 삭제 성공");
    }

    // 모아보기에서 북마크 삭제
    @Transactional
    public Message deleteBookmarkContent(BookmarkContentListRequest request) {
        Account currentAccount = getCurrentAccount(accountRepository);
        Bookmark bookmark = bookmarkRepository
                .findByAccountAndFolderNameAndBookmarkType(currentAccount, INIT_BOOKMARK_FOLDER_NAME, request.bookmarkType())
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, currentAccount.getId()));
        if (request.bookmarkType().equals(BookmarkType.BOOKSTORE)) {
            for (Long contentId : request.contentIdList()) {
                bookmarkBookStoreRepository.findByBookStoreIdAndBookmark(contentId, bookmark)
                        .ifPresent(
                                bookmarkBookStore -> {
                                    bookmark.removeBookmarkBookStore(bookmarkBookStore);
                                    bookmarkBookStore.getBookStore().removeBookmarkBookStore(bookmarkBookStore);
                                }
                        );
                bookmarkBookStoreRepository.deleteByBookStoreIdAndBookmarkId(contentId, bookmark.getId());
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
                bookmarkArticleRepository.deleteByArticleIdAndBookmarkId(contentId, bookmark.getId());
                bookmark.updateBookmarkArticle(bookmarkArticleRepository.findAllByBookmark(bookmark));
            }
        }

        return Message.of("북마크 삭제 완료");
    }

    // 북마크 폴더 삭제
    @Transactional
    public Message deleteBookmarkFolder(Long bookmarkId) {
        Account currentAccount = getCurrentAccount(accountRepository);
        Bookmark bookmark = bookmarkRepository.findByIdAndAccount(bookmarkId, currentAccount)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, bookmarkId));
        if (bookmark.getFolderName().equals(INIT_BOOKMARK_FOLDER_NAME)) {
            throw new BookmarkException(ErrorCode.NOT_DELETE_INIT_BOOKMARK, bookmarkId);
        }

        // 폴더 내용은 북마크 모아보기로 이동
        if (bookmark.getBookmarkType().equals(BookmarkType.BOOKSTORE)) {
            Bookmark bookmarkCollect = bookmarkRepository
                    .findByAccountAndFolderNameAndBookmarkType(currentAccount, INIT_BOOKMARK_FOLDER_NAME, BookmarkType.BOOKSTORE)
                    .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, currentAccount.getId()));

            List<Long> collectBookmarkBookStoreList = bookmarkCollect.getBookmarkBookStoreList().stream()
                    .map(bookmarkBookStore -> bookmarkBookStore.getBookStore().getId())
                    .toList();

            log.info(collectBookmarkBookStoreList.toString());

            List<Long> currentBookmarkBookStoreList = bookmarkBookStoreRepository.findAllByBookmark(bookmark).stream()
                    .map(bookmarkBookStore -> bookmarkBookStore.getBookStore().getId())
                    .toList();

            log.info(currentBookmarkBookStoreList.toString());


            // 모아보기에 없을 시 추가
            for (Long bookmarkBookStoreId : currentBookmarkBookStoreList) {
                if (!collectBookmarkBookStoreList.contains(bookmarkBookStoreId)) {
                    BookmarkBookStore bookStore = BookmarkBookStore.builder()
                            .bookmark(bookmarkCollect)
                            .bookStore(bookStoreRepository.findById(bookmarkBookStoreId).orElseThrow(
                                    () -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, bookmarkBookStoreId)))
                            .build();
                    bookmarkBookStoreRepository.save(bookStore);
                }
            }

        } else {
            Bookmark bookmarkCollect = bookmarkRepository
                    .findByAccountAndFolderNameAndBookmarkType(currentAccount, INIT_BOOKMARK_FOLDER_NAME, BookmarkType.ARTICLE)
                    .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, currentAccount.getId()));

            List<Long> collectBookmarkArticleList = bookmarkCollect.getBookmarkArticleList().stream()
                    .map(bookmarkArticle -> bookmarkArticle.getArticle().getId())
                    .toList();

            List<Long> currentBookmarkArticleList = bookmarkArticleRepository.findAllByBookmark(bookmark).stream()
                    .map(bookmarkArticle -> bookmarkArticle.getArticle().getId())
                    .toList();

            for (Long bookmarkArticleId : currentBookmarkArticleList) {
                if (!collectBookmarkArticleList.contains(bookmarkArticleId)) {
                    BookmarkArticle article = BookmarkArticle.builder()
                            .bookmark(bookmarkCollect)
                            .article(articleRepository.findById(bookmarkArticleId).orElseThrow(
                                    () -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, bookmarkArticleId)))
                            .build();
                    bookmarkArticleRepository.save(article);
                }
            }
        }

        bookmarkRepository.delete(bookmark);
        return Message.of("북마크 폴더 삭제 완료");
    }

    // 모아보기 북마크 폴더 내용 조회
    public BookmarkResponse getBookmarkCollect(String bookmarkType, Pageable pageable, Long cursorId) {
        Account currentAccount = getCurrentAccount(accountRepository);
        BookmarkType bookmarkTypeEnum = BookmarkType.valueOf(bookmarkType.toUpperCase());
        Bookmark bookmark = bookmarkRepository
                .findByAccountAndFolderNameAndBookmarkType(currentAccount, INIT_BOOKMARK_FOLDER_NAME, bookmarkTypeEnum)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, bookmarkType));

        return getBookmarkResponse(bookmark, pageable, cursorId);
    }

    private BookmarkResponse getBookmarkResponse(Bookmark bookmark, Pageable pageable, Long cursorId) {
        if (bookmark.getBookmarkType().equals(BookmarkType.BOOKSTORE)) {
            Optional<BookmarkBookStore> firstByBookmarkId = bookmarkBookStoreRepository.findFirstByBookmarkId(bookmark.getId());
            Long nextCursorId = cursorId != null && cursorId == 0 && firstByBookmarkId.isPresent()
                    ? firstByBookmarkId.get().getBookStore().getId() : cursorId;
            String createdAt = cursorId != null && cursorId == 0 && firstByBookmarkId.isPresent()
                    ? firstByBookmarkId.get().getBookStore().getCreatedAt() : cursorId == null ? null : getBookmarkBookStore(nextCursorId).getCreatedAt();
            Page<BookmarkInfo> page = bookmarkBookStoreRepository.findAllByBookmarkAndAndVisibilityTrue(bookmark, pageable, cursorId, createdAt)
                    .map(bookmarkBookStore -> BookmarkInfo.ofBookStore(bookmarkBookStore.getBookStore()));
            Long totalElements = bookmarkBookStoreRepository.countAllByBookmark(bookmark);
            PageResponse<BookmarkInfo> bookmarkInfoPageResponse = PageResponse.ofCursor(page, totalElements);
            return BookmarkResponse.of(bookmark, bookmarkInfoPageResponse);

        }else {
            Optional<BookmarkArticle> firstByBookmarkId = bookmarkArticleRepository.findFirstByBookmarkId(bookmark.getId());
            Long nextCursorId = cursorId != null && cursorId == 0 && firstByBookmarkId.isPresent()
                    ? firstByBookmarkId.get().getArticle().getId() : cursorId;
            String createdAt = cursorId != null && cursorId == 0 && firstByBookmarkId.isPresent()
                    ? firstByBookmarkId.get().getArticle().getCreatedAt() : cursorId == null ? null : getBookmarkArticle(nextCursorId).getCreatedAt();
            Page<BookmarkInfo> page = bookmarkArticleRepository.findAllByBookmarkAndAndVisibilityTrue(bookmark, pageable, cursorId, createdAt)
                    .map(bookmarkArticle -> BookmarkInfo.ofArticle(bookmarkArticle.getArticle()));
            Long totalElements = bookmarkArticleRepository.countAllByBookmark(bookmark);
            PageResponse<BookmarkInfo> bookmarkInfoPageResponse = PageResponse.ofCursor(page, totalElements);
            return BookmarkResponse.of(bookmark, bookmarkInfoPageResponse);
        }
    }

    // 특정 아티클 or 서점이 북마크에 있는지 확인
    public boolean isBookmark(Long contentId, String bookmarkType) {
        Account currentAccount = getCurrentAccount(accountRepository);
        BookmarkType bookmarkTypeEnum = BookmarkType.valueOf(bookmarkType.toUpperCase());
        Bookmark bookmark = currentAccount.getBookmarkList().stream()
                .filter(b -> b.getFolderName().equals(INIT_BOOKMARK_FOLDER_NAME))
                .filter(b -> b.getBookmarkType().equals(bookmarkTypeEnum))
                .findFirst()
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK, bookmarkType));
        if (bookmarkTypeEnum.equals(BookmarkType.BOOKSTORE)) {
            return bookmark.getBookmarkBookStoreList().stream()
                    .anyMatch(bookmarkBookStore -> bookmarkBookStore.getBookStore().getId().equals(contentId));
        } else {
            return bookmark.getBookmarkArticleList().stream()
                    .anyMatch(bookmarkArticle -> bookmarkArticle.getArticle().getId().equals(contentId));
        }
    }
}
