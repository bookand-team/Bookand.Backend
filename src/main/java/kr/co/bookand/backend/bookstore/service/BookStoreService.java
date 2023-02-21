package kr.co.bookand.backend.bookstore.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.ArticleBookStore;
import kr.co.bookand.backend.article.domain.dto.ArticleDto;
import kr.co.bookand.backend.article.repository.ArticleBookStoreRepository;
import kr.co.bookand.backend.bookmark.domain.BookmarkType;
import kr.co.bookand.backend.bookmark.service.BookmarkService;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.BookStoreImage;
import kr.co.bookand.backend.bookstore.domain.BookstoreTheme;
import kr.co.bookand.backend.bookstore.domain.ReportBookStore;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreImageDto;
import kr.co.bookand.backend.bookstore.exception.BookStoreException;
import kr.co.bookand.backend.bookstore.repository.BookStoreImageRepository;
import kr.co.bookand.backend.bookstore.repository.BookStoreRepository;
import kr.co.bookand.backend.bookstore.repository.ReportBookStoreRepository;
import kr.co.bookand.backend.common.domain.Status;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import kr.co.bookand.backend.common.exception.ErrorCode;
import kr.co.bookand.backend.common.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static kr.co.bookand.backend.article.domain.dto.ArticleDto.*;
import static kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto.*;
import static kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto.BookStoreResponse.of;
import static kr.co.bookand.backend.bookstore.domain.dto.BookStoreImageDto.*;
import static kr.co.bookand.backend.common.domain.dto.PageStateDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookStoreService {

    private final BookStoreRepository bookStoreRepository;
    private final BookStoreImageRepository bookStoreImageRepository;
    private final ArticleBookStoreRepository articleBookStoreRepository;
    private final ReportBookStoreRepository reportBookStoreRepository;
    private final AccountService accountService;
    private final BookmarkService bookmarkService;

    @Transactional
    public BookStoreResponse createBookStore(BookStoreRequest bookStoreRequest) {
        accountService.isAccountAdmin();
        duplicateBookStoreName(bookStoreRequest.name());
        List<String> subImageList = bookStoreRequest.subImage();
        List<BookStoreImage> bookStoreImageList = new ArrayList<>();

        subImageList.stream().map(image -> BookStoreImage.builder().url(image).build()).forEach(bookStoreImage -> {
            bookStoreImageRepository.save(bookStoreImage);
            bookStoreImageList.add(bookStoreImage);
        });

        BookStore bookStore = bookStoreRequest.toEntity(bookStoreImageList);
        bookStoreImageList.forEach(bookStoreImage -> bookStoreImage.updateBookStore(bookStore));
        BookStore saveBookStore = bookStoreRepository.save(bookStore);
        return of(saveBookStore, false, null);
    }

    // 상세 조회 (APP)
    public BookStoreResponse getBookStore(Long id) {
        boolean isBookmark = bookmarkService.isBookmark(id, BookmarkType.BOOKSTORE.name());
        BookStore findBookStore = bookStoreRepository.findById(id).orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, id));

        List<ArticleSimpleResponse> articleList = findBookStore.getArticleBookStoreList().stream()
                .filter(articleBookStore -> articleBookStore.getArticle().getStatus().equals(Status.VISIBLE))
                .map(ArticleBookStore::getArticle)
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

    @Transactional
    public BookStorePageResponse searchBookStoreList(PageStateRequest pageStateRequest) {
        Pageable pageable = PageRequest.of(pageStateRequest.page() - 1, pageStateRequest.row());
        String search = pageStateRequest.search();
        String bookstoreTheme = pageStateRequest.theme();
        BookstoreTheme theme = BookstoreTheme.valueOf(bookstoreTheme);
        String bookstoreStatus = pageStateRequest.status();
        Status status = Status.valueOf(bookstoreStatus);
        Page<BookStoreWebResponse> bookStorePage;
        if (search == null && theme == null && status == null) {
            bookStorePage = bookStoreRepository.findAll(pageable).map(BookStoreWebResponse::of);
        } else if (search == null && status == null) {
            bookStorePage = bookStoreRepository.findAllByTheme(theme, pageable).map(BookStoreWebResponse::of);
        } else if (search == null && theme == null) {
            bookStorePage = bookStoreRepository.findAllByStatus(status, pageable).map(BookStoreWebResponse::of);
        } else if (theme == null && status == null) {
            bookStorePage = bookStoreRepository.findAllByNameContaining(search, pageable).map(BookStoreWebResponse::of);
        } else if (status == null) {
            bookStorePage = bookStoreRepository.findAllByNameContainingAndTheme(search, theme, pageable).map(BookStoreWebResponse::of);
        } else if (theme == null) {
            bookStorePage = bookStoreRepository.findAllByNameContainingAndStatus(search, status, pageable).map(BookStoreWebResponse::of);
        } else if (search == null) {
            bookStorePage = bookStoreRepository.findAllByThemeAndStatus(theme, status, pageable).map(BookStoreWebResponse::of);
        } else {
            bookStorePage = bookStoreRepository.findAllByNameContainingAndThemeAndStatus(search, theme, status, pageable).map(BookStoreWebResponse::of);
        }

        return BookStorePageResponse.of(bookStorePage);
    }

    @Transactional
    public BookStoreWebResponse updateBookStore(Long bookStoreId, BookStoreRequest bookStoreRequest) {
        accountService.isAccountAdmin();
        BookStore bookStore = bookStoreRepository.findById(bookStoreId).orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, bookStoreId));
        List<BookStoreImage> subImages = bookStore.getSubImages();
        bookStoreImageRepository.deleteAll(subImages);
        for (String id : bookStoreRequest.subImage()) {
            BookStoreImage image = BookStoreImage.builder()
                    .url(id)
                    .bookStore(bookStore)
                    .build();
            bookStoreImageRepository.save(image);
        }
        duplicateBookStoreName(bookStoreRequest.name());
        bookStore.updateBookStoreData(bookStoreRequest);
        return BookStoreWebResponse.of(bookStore);
    }

    @Transactional
    public void deleteBookStore(Long id) {
        accountService.isAccountAdmin();
        BookStore bookStore = bookStoreRepository.findById(id).orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, id));
        bookStore.softDelete();
    }

    @Transactional
    public Message deleteBookStoreList(BookStoreListRequest list) {
        accountService.isAccountAdmin();
        for (Long id : list.bookStoreDtoList()) {
            BookStore bookStore = bookStoreRepository.findById(id).orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, id));
            bookStore.softDelete();
        }
        return Message.of("삭제완료");
    }

    @Transactional
    public BookStoreWebResponse updateBookStoreStatus(Long id) {
        accountService.isAccountAdmin();
        BookStore bookStore = bookStoreRepository.findById(id).orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, id));
        bookStore.updateBookStoreStatus(bookStore.getStatus() == Status.VISIBLE ? Status.INVISIBLE : Status.VISIBLE);
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
        ReportBookStore reportBookStore = reportBookStoreRepository.findById(reportId).orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE_REPORT, reportId));
        reportBookStore.updateAnswer(answerReportRequest);
        return Message.of("답변 완료");
    }

    public PageResponse<BookStoreReportList> getBookStoreReportList(Pageable pageable) {
        accountService.isAccountAdmin();
        List<BookStoreReportList> bookStoreReportLists = reportBookStoreRepository.findAll().stream().map(BookStoreReportList::of).toList();
        return PageResponse.of(pageable, bookStoreReportLists);
    }
}
