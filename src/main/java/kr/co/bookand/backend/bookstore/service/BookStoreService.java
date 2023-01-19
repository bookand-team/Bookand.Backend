package kr.co.bookand.backend.bookstore.service;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreListDto;
import kr.co.bookand.backend.bookstore.domain.dto.BookStorePageDto;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreSearchDto;
import kr.co.bookand.backend.bookstore.exception.BookStoreException;
import kr.co.bookand.backend.bookstore.repository.BookStoreRepository;
import kr.co.bookand.backend.common.exception.ErrorCode;
import kr.co.bookand.backend.common.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookStoreService {

    private final BookStoreRepository bookStoreRepository;

    public BookStoreDto createBookStore(BookStoreDto bookStoreDto) {
        BookStore bookStore = bookStoreDto.toBookStore();
        BookStore saveBookStore = bookStoreRepository.save(bookStore);
        return BookStoreDto.of(saveBookStore);
    }

    public BookStoreDto getBookStore(Long id) {
        return bookStoreRepository.findById(id).map(BookStoreDto::of).orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, id));
    }

    public BookStoreDto updateBookStore(BookStoreDto bookStoreDto) {
        Long bookStoreId = bookStoreDto.getId();
        BookStore bookStore = bookStoreRepository.findById(bookStoreId).orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, bookStoreId));
        // 검증 로직 추가
        bookStore.updateBookStoreData(bookStoreDto);
        return BookStoreDto.of(bookStore);
    }

    public void removeBookStore(Long id) {
        BookStore bookStore = bookStoreRepository.findById(id).orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, id));
        bookStoreRepository.delete(bookStore);
    }

    public BookStorePageDto searchBookStoreList(BookStoreSearchDto bookStoreSearchDto) {
        List<BookStoreDto> result = new ArrayList<>();
        Pageable pageable = PageRequest.of(bookStoreSearchDto.getPage(), bookStoreSearchDto.getRaw());
        Page<BookStore> byStatusAndThemeAndName = bookStoreRepository.findByStatusAndThemeAndName(bookStoreSearchDto.getStatus(), bookStoreSearchDto.getCategory(), bookStoreSearchDto.getSearch(), pageable);
        byStatusAndThemeAndName
                .forEach(bookStore -> result.add(BookStoreDto.of(bookStore)));

        int totalPages = byStatusAndThemeAndName.getTotalPages();
        int number = byStatusAndThemeAndName.getNumber();

        return BookStorePageDto.of(result, totalPages, number);
    }

    public Message deleteBookStoreList(BookStoreListDto list) {
        list.getBookStoreDtoList().forEach(bookStoreRepository::deleteById);
        // 예외처리
        return Message.of("삭제완료");
    }
}
