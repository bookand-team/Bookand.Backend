package kr.co.bookand.backend.bookstore.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreListDto;
import kr.co.bookand.backend.bookstore.domain.dto.BookStorePageDto;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreSearchDto;
import kr.co.bookand.backend.bookstore.service.BookStoreService;
import kr.co.bookand.backend.common.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/bookstore")
@RequiredArgsConstructor
@Api(tags = "서점 API")
public class BookStoreController {

    private final BookStoreService bookStoreService;

    @ApiOperation(value = "서점 등록")
    @PostMapping("")
    public BookStoreDto createBookStore(@RequestBody BookStoreDto bookStoreDto) {
        return bookStoreService.createBookStore(bookStoreDto);
    }

    @ApiOperation(value = "서점 상세 조회")
    @GetMapping("/{id}")
    public BookStoreDto findBookStore(@PathVariable Long id) {
        return bookStoreService.getBookStore(id);
    }

    @ApiOperation(value = "서점 수정")
    @PutMapping("")
    public BookStoreDto updateBookStore(@RequestBody BookStoreDto bookStoreDto) {
        return bookStoreService.updateBookStore(bookStoreDto);
    }

    @ApiOperation(value = "서점 삭제")
    @DeleteMapping("/{id}")
    public Message deleteBookStore(@PathVariable Long id) {
        bookStoreService.removeBookStore(id);
        return Message.of("서점 삭제");
    }

    @ApiOperation(value = "조건에 맞는 서점들 조회")
    @PostMapping("/search")
    public BookStorePageDto findBookStoreByCriteria(@RequestBody BookStoreSearchDto bookStoreSearchDto) {
        return bookStoreService.searchBookStoreList(bookStoreSearchDto);
    }

    @ApiOperation(value = "선택된 서점 삭제")
    @DeleteMapping("/list")
    public Message deleteBookStoreList(@RequestBody BookStoreListDto list) {
        return bookStoreService.deleteBookStoreList(list);
    }
}
