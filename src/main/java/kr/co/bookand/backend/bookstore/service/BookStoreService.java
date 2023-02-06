package kr.co.bookand.backend.bookstore.service;

import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.BookStoreImage;
import kr.co.bookand.backend.bookstore.domain.BookstoreTheme;
import kr.co.bookand.backend.bookstore.exception.BookStoreException;
import kr.co.bookand.backend.bookstore.repository.BookStoreImageRepository;
import kr.co.bookand.backend.bookstore.repository.BookStoreRepository;
import kr.co.bookand.backend.common.domain.Status;
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

import static kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto.*;
import static kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto.BookStoreResponse.of;
import static kr.co.bookand.backend.common.domain.PageStateDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookStoreService {

    private final BookStoreRepository bookStoreRepository;
    private final BookStoreImageRepository bookStoreImageRepository;
    private final AccountService accountService;

    @Transactional
    public BookStoreResponse createBookStore(BookStoreRequest bookStoreRequest) {
        accountService.isAccountAdmin();
        duplicateBookStoreName(bookStoreRequest.name());
        List<String> subImageList = bookStoreRequest.subImage();
        List<BookStoreImage> bookStoreImageList = new ArrayList<>();
        for (String image : subImageList) {
            BookStoreImage bookStoreImage = BookStoreImage.builder()
                    .url(image)
                    .build();
            bookStoreImageRepository.save(bookStoreImage);
            bookStoreImageList.add(bookStoreImage);
        }

        BookStore bookStore = bookStoreRequest.toEntity(bookStoreImageList);
        BookStore saveBookStore = bookStoreRepository.save(bookStore);
        return of(saveBookStore);
    }

    public BookStoreResponse getBookStore(Long id) {
        return bookStoreRepository.findById(id).map(BookStoreResponse::of).orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, id));
    }

    public void duplicateBookStoreName(String name) {
        if (bookStoreRepository.existsByName(name))
            throw new BookStoreException(ErrorCode.DUPLICATE_BOOKSTORE_NAME, name);
    }

    @Transactional
    public BookStorePageResponse searchBookStoreList(PageStateRequest pageStateRequest) {
        Pageable pageable = PageRequest.of(pageStateRequest.page() - 1, pageStateRequest.row());
        String search = pageStateRequest.search();
        String bookstoreTheme = pageStateRequest.theme();
        BookstoreTheme theme = BookstoreTheme.valueOf(bookstoreTheme);
        String bookstoreStatus = pageStateRequest.status();
        Status status = Status.valueOf(bookstoreStatus);
        Page<BookStoreResponse> bookStorePage;
        if (search == null && theme == null && status == null) {
            bookStorePage = bookStoreRepository.findAll(pageable).map(BookStoreResponse::of);
        } else if (search == null && theme != null && status == null) {
            bookStorePage = bookStoreRepository.findAllByTheme(theme, pageable).map(BookStoreResponse::of);
        } else if (search == null && theme == null && status != null) {
            bookStorePage = bookStoreRepository.findAllByStatus(status, pageable).map(BookStoreResponse::of);
        } else if (search != null && theme == null && status == null) {
            bookStorePage = bookStoreRepository.findAllByName(search, pageable).map(BookStoreResponse::of);
        } else if (search != null && theme != null && status == null) {
            bookStorePage = bookStoreRepository.findAllByNameAndTheme(search, theme, pageable).map(BookStoreResponse::of);
        } else if (search != null && theme == null && status != null) {
            bookStorePage = bookStoreRepository.findAllByNameAndStatus(search, status, pageable).map(BookStoreResponse::of);
        } else if (search == null && theme != null && status != null) {
            bookStorePage = bookStoreRepository.findAllByThemeAndStatus(theme, status, pageable).map(BookStoreResponse::of);
        } else if (search != null && theme != null && status != null) {
            bookStorePage = bookStoreRepository.findAllByNameAndThemeAndStatus(search, theme, status, pageable).map(BookStoreResponse::of);
        }  else {
            throw new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, "검색 조건이 잘못되었습니다.");
        }

        return BookStorePageResponse.of(bookStorePage);
    }

    @Transactional
    public BookStoreResponse updateBookStore(Long bookStoreId, BookStoreRequest bookStoreRequest) {
        accountService.isAccountAdmin();
        BookStore bookStore = bookStoreRepository.findById(bookStoreId).orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, bookStoreId));
        bookStore.updateBookStoreData(bookStoreRequest);
        return BookStoreResponse.of(bookStore);
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

    public BookStorePageResponse getBookStoreList(Pageable pageable) {
        Page<BookStoreResponse> bookStorePage = bookStoreRepository.findAll(pageable).map(BookStoreResponse::of);
        return BookStorePageResponse.of(bookStorePage);
    }

    @Transactional
    public BookStoreResponse updateBookStoreVisible(Long id) {
        accountService.isAccountAdmin();
        BookStore bookStore = bookStoreRepository.findById(id).orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, id));
        bookStore.updateBookStoreStatus(
                bookStore.getStatus() == Status.VISIBLE ? Status.INVISIBLE : Status.VISIBLE
        );
        return BookStoreResponse.of(bookStore);
    }
}
