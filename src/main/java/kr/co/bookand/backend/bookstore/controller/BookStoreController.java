package kr.co.bookand.backend.bookstore.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "서점 API")
public class BookStoreController {

    private final BookStoreService bookStoreService;

    @ApiOperation(value = "서점 이름 조회")
    @GetMapping("/{name}")
    public BookStoreDto findBookStore(@PathVariable String name) {
        return bookStoreService.getBookStore(name);
    }

    @ApiOperation(value = "서점 전체 조회")
    @GetMapping("")
    public Page<BookStoreDto> findAllBookStore(@PageableDefault(size = 10) Pageable pageable) {
        return bookStoreService.getBookStoreList(pageable);
    }

    @ApiOperation(value = "서점 생성")
    @PostMapping("")
    public BookStoreDto createBookStore(@RequestBody BookStoreDto bookStoreDto) {
        return bookStoreService.createBookStore(bookStoreDto);
    }

    @ApiOperation(value = "서점 수정")
    @PutMapping("/{id}")
    public BookStoreDto updateBookStore(@PathVariable String id, @RequestBody BookStoreDto bookStoreDto) {
        return bookStoreService.updateBookStore(id, bookStoreDto);
    }

    @ApiOperation(value = "서점 삭제")
    @DeleteMapping("/{name}")
    public Message deleteBookStore(@PathVariable String name) {
        bookStoreService.removeBookStore(name);
        return Message.of("서점 삭제");
    }
}
