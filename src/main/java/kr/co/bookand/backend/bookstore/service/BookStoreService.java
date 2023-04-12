package kr.co.bookand.backend.bookstore.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.ArticleBookStore;
import kr.co.bookand.backend.bookmark.domain.BookmarkType;
import kr.co.bookand.backend.bookmark.service.BookmarkService;
import kr.co.bookand.backend.bookstore.domain.*;
import kr.co.bookand.backend.bookstore.exception.BookStoreException;
import kr.co.bookand.backend.bookstore.repository.*;
import kr.co.bookand.backend.common.domain.Status;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import kr.co.bookand.backend.common.exception.ErrorCode;
import kr.co.bookand.backend.common.domain.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static kr.co.bookand.backend.article.domain.dto.ArticleDto.*;
import static kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto.*;
import static kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto.BookStoreResponse.of;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookStoreService {

    private final BookStoreRepository bookStoreRepository;
    private final BookStoreImageRepository bookStoreImageRepository;
    private final BookStoreThemeRepository bookStoreThemeRepository;
    private final BookStoreVersionRepository bookStoreVersionRepository;
    private final ReportBookStoreRepository reportBookStoreRepository;
    private final AccountService accountService;
    private final BookmarkService bookmarkService;

    @Transactional
    public BookStoreResponse createBookStore(BookStoreRequest bookStoreRequest) {
        accountService.isAccountAdmin();
        duplicateBookStoreName(bookStoreRequest.name());
        List<String> theme = bookStoreRequest.themeList();
        List<String> subImageList = bookStoreRequest.subImage();
        List<BookStoreImage> bookStoreImageList = new ArrayList<>();
        List<BookStoreTheme> bookStoreThemeList = new ArrayList<>();
        checkCountBookStoreTheme(theme);
        subImageList.stream().map(image -> BookStoreImage.builder().url(image).build()).forEach(bookStoreImage -> {
            bookStoreImageRepository.save(bookStoreImage);
            bookStoreImageList.add(bookStoreImage);
        });

        theme.stream().map(it -> BookStoreTheme.builder().theme(BookStoreType.valueOf(it)).build())
                .forEach(bookStoreTheme -> {
                    bookStoreThemeRepository.save(bookStoreTheme);
                    bookStoreThemeList.add(bookStoreTheme);
                });
        BookStoreVersion newBookStoreVersion = createBookStoreVersion();
        BookStore bookStore = bookStoreRequest.toEntity(bookStoreImageList, bookStoreThemeList, newBookStoreVersion);
        bookStoreImageList.forEach(bookStoreImage -> bookStoreImage.updateBookStore(bookStore));
        bookStoreThemeList.forEach(bookStoreTheme -> bookStoreTheme.updateBookStore(bookStore));
        BookStore saveBookStore = bookStoreRepository.save(bookStore);
        updateBookStoreVersion(newBookStoreVersion);
        return of(saveBookStore, false, null);
    }

    public void checkCountBookStoreTheme(List<String> theme) {
        if (theme.size() > 4) {
            throw new BookStoreException(ErrorCode.TOO_MANY_BOOKSTORE_THEME, theme.size());
        }
    }

    // 상세 조회 (APP)
    public BookStoreResponse getBookStore(Long id) {
        boolean isBookmark = bookmarkService.isBookmark(id, BookmarkType.BOOKSTORE.name());
        BookStore findBookStore = bookStoreRepository.findById(id)
                .orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, id));

        List<ArticleSimpleResponse> articleList = findBookStore.getArticleBookStoreList().stream()
                .map(ArticleBookStore::getArticle)
                .filter(article -> article.getStatus().equals(Status.VISIBLE))
                .map((Article article) -> {
                    boolean isBookmarkArticle = bookmarkService.isBookmark(article.getId(), BookmarkType.ARTICLE.name());
                    return ArticleSimpleResponse.of(article, isBookmarkArticle);
                })
                .toList();

        return of(findBookStore, isBookmark, articleList);
    }

    public void duplicateBookStoreName(String name) {
        if (bookStoreRepository.existsByName(name))
            throw new BookStoreException(ErrorCode.DUPLICATE_BOOKSTORE_NAME, name);
    }

    // 전체 조회 (WEB)
    public BookStorePageResponse getBookStoreList(Pageable pageable) {
        Page<BookStoreWebResponse> bookStorePage = bookStoreRepository.findAll(pageable).map(BookStoreWebResponse::of);
        return BookStorePageResponse.of(bookStorePage);
    }

    // 전체 조회 (APP)
    public BookStorePageResponseApp getBookStoreListApp(Pageable pageable) {
        Page<BookStoreSimpleResponse> bookStorePage = bookStoreRepository.findAllByStatus(Status.VISIBLE, pageable)
                .map((BookStore bookStore) -> {
                    boolean isBookmark = bookmarkService.isBookmark(bookStore.getId(), BookmarkType.BOOKSTORE.name());
                    return BookStoreSimpleResponse.of(bookStore, isBookmark);
                });
        return BookStorePageResponseApp.of(bookStorePage);
    }

    // 조건 검색
    @Transactional
    public BookStorePageResponse searchBookStoreList(String search, String theme, String status, Pageable pageable) {
        Page<BookStoreWebResponse> bookStorePage = bookStoreRepository.findAllBySearch(search, theme, status, pageable)
                .map(BookStoreWebResponse::of);
        return BookStorePageResponse.of(bookStorePage);
    }

    @Transactional
    public BookStoreWebResponse updateBookStore(Long bookStoreId, BookStoreRequest bookStoreRequest) {
        accountService.isAccountAdmin();
        BookStore bookStore = bookStoreRepository.findById(bookStoreId)
                .orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, bookStoreId));
        List<BookStoreImage> subImages = bookStore.getSubImages();
        bookStoreImageRepository.deleteAll(subImages);
        for (String id : bookStoreRequest.subImage()) {
            BookStoreImage image = BookStoreImage.builder()
                    .url(id)
                    .bookStore(bookStore)
                    .build();
            bookStoreImageRepository.save(image);
        }

        List<BookStoreTheme> themes = bookStore.getThemeList();
        bookStoreThemeRepository.deleteAll(themes);
        for (String theme : bookStoreRequest.themeList()) {
            BookStoreTheme bookStoreTheme = BookStoreTheme.builder()
                    .theme(BookStoreType.valueOf(theme))
                    .bookStore(bookStore)
                    .build();
            bookStoreThemeRepository.save(bookStoreTheme);
        }
        duplicateBookStoreName(bookStoreRequest.name());
        bookStore.updateBookStoreData(bookStoreRequest);
        updateBookStoreVersion();
        return BookStoreWebResponse.of(bookStore);
    }

    @Transactional
    public void deleteBookStore(Long id) {
        accountService.isAccountAdmin();
        BookStore bookStore = bookStoreRepository.findById(id)
                .orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, id));
        bookStore.softDelete();
        updateBookStoreVersion();
    }

    @Transactional
    public Message deleteBookStoreList(BookStoreListRequest list) {
        accountService.isAccountAdmin();
        for (Long id : list.bookStoreDtoList()) {
            BookStore bookStore = bookStoreRepository.findById(id)
                    .orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, id));
            bookStore.softDelete();
        }
        updateBookStoreVersion();
        return Message.of("삭제완료");
    }

    @Transactional
    public BookStoreWebResponse updateBookStoreStatus(Long id) {
        accountService.isAccountAdmin();
        BookStore bookStore = bookStoreRepository.findById(id)
                .orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, id));
        bookStore.updateBookStoreStatus(bookStore.getStatus() == Status.VISIBLE ? Status.INVISIBLE : Status.VISIBLE);
        bookStore.updateDisplayDate(LocalDateTime.now());
        updateBookStoreVersion();
        return BookStoreWebResponse.of(bookStore);
    }

    @Transactional
    public Message reportBookStore(ReportBookStoreRequest reportBookStoreRequest) {
        Account account = accountService.checkAccountUser();
        ReportBookStore reportBookStore = reportBookStoreRequest.toEntity(account);
        reportBookStoreRepository.save(reportBookStore);
        return Message.of("제보 완료");
    }

    @Transactional
    public Message answerReportBookStore(Long reportId, AnswerReportRequest answerReportRequest) {
        accountService.isAccountAdmin();
        ReportBookStore reportBookStore = reportBookStoreRepository.findById(reportId)
                .orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE_REPORT, reportId));
        reportBookStore.updateAnswer(answerReportRequest);
        return Message.of("답변 완료");
    }

    public PageResponse<BookStoreReportListResponse> getBookStoreReportList(Pageable pageable) {
        accountService.isAccountAdmin();
        Page<BookStoreReportListResponse> bookStoreReportList = reportBookStoreRepository.findAll(pageable).map(BookStoreReportListResponse::of);
        return PageResponse.of(bookStoreReportList);
    }

    public BookStoreVersionListResponse checkBookStoreVersion(Long versionId) {
        Long currentVersionId = bookStoreVersionRepository.findFirstByOrderByIdDesc().getId();
        List<BookStoreVersionResponse> bookStoreList = new ArrayList<>();
        if (versionId < currentVersionId) {
            bookStoreList = bookStoreRepository.findAllByStatus(Status.VISIBLE)
                    .stream()
                    .map(it -> BookStoreVersionResponse.of(it, bookmarkService.isBookmark(it.getId(), BookmarkType.BOOKSTORE.toString())))
                    .toList();
        }
        return BookStoreVersionListResponse.of(bookStoreList, currentVersionId);
    }

    @Transactional
    public BookStoreVersion createBookStoreVersion() {
        return bookStoreVersionRepository.save(BookStoreVersion.builder().build());
    }

    @Transactional
    public void updateBookStoreVersion() {
        BookStoreVersion newVersion = bookStoreVersionRepository.save(BookStoreVersion.builder().build());
        bookStoreRepository.findAll().forEach(it -> it.updateBookStoreVersion(newVersion));
    }

    @Transactional
    public void updateBookStoreVersion(BookStoreVersion newVersion) {
        bookStoreRepository.findAll().forEach(it -> it.updateBookStoreVersion(newVersion));
    }

}
