package kr.co.bookand.backend.bookstore.controller;

import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto;
import kr.co.bookand.backend.bookstore.service.BookStoreService;
import kr.co.bookand.backend.common.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/bookstore")
@RequiredArgsConstructor
public class BookStoreController {

    private final BookStoreService bookStoreService;

    @GetMapping("/{name}")
    public BookStoreDto findBookStore(@PathVariable String name) {
        return bookStoreService.getBookStore(name);
    }

    @GetMapping("")
    public Page<BookStoreDto> findAllBookStore(@PageableDefault(size = 10) Pageable pageable) {
        return bookStoreService.getBookStoreList(pageable);
    }

    @PostMapping("")
    public BookStoreDto createBookStore(@RequestBody BookStoreDto bookStoreDto) {
        return bookStoreService.createBookStore(bookStoreDto);
    }

    @PutMapping("")
    public BookStoreDto updateBookStore(@RequestBody BookStoreDto bookStoreDto) {
        return bookStoreService.updateBookStore(bookStoreDto);
    }

    @DeleteMapping("/{name}")
    public Message deleteBookStore(@PathVariable String name) {
        bookStoreService.removeBookStore(name);
        return Message.of("서점 삭제");
    }
}
