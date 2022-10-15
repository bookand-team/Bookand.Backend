package kr.co.bookand.backend.bookstore.service;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto;
import kr.co.bookand.backend.bookstore.repository.BookStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookStoreService {

    private final BookStoreRepository bookStoreRepository;

    public BookStoreDto getBookStore(String name) {
        return bookStoreRepository.findByName(name).map(BookStoreDto::of).orElseThrow(RuntimeException::new);
    }

    public Page<BookStoreDto> getBookStoreList(Pageable pageable){
        Page<BookStore> bookStorePage = bookStoreRepository.findAll(pageable);
        return bookStorePage.map(BookStoreDto::of);
    }

    public BookStoreDto createBookStore(BookStoreDto bookStoreDto) {
        BookStore bookStore = bookStoreDto.toBookStore();
        BookStore saveBookStore = bookStoreRepository.save(bookStore);
        return BookStoreDto.of(saveBookStore);
    }

    public BookStoreDto updateBookStore(BookStoreDto bookStoreDto) {
        String name = bookStoreDto.getName();
        BookStore bookStore = bookStoreRepository.findByName(name).orElseThrow(RuntimeException::new);
        bookStore.updateBookStoreData(bookStoreDto);
        return BookStoreDto.of(bookStore);
    }

    public void removeBookStore(String name) {
        BookStore bookStore = bookStoreRepository.findByName(name).orElseThrow(RuntimeException::new);
        bookStoreRepository.delete(bookStore);
    }
}
