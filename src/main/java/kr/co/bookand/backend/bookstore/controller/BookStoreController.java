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
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BookStoreDto> createBookStore(@RequestBody BookStoreDto bookStoreDto) {
        return ResponseEntity.ok(bookStoreService.createBookStore(bookStoreDto));
    }

    @ApiOperation(value = "서점 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<BookStoreDto> findBookStore(@PathVariable Long id) {
        return ResponseEntity.ok(bookStoreService.getBookStore(id));
    }

    @ApiOperation(value = "서점 수정")
    @PutMapping("")
    public ResponseEntity<BookStoreDto> updateBookStore(@RequestBody BookStoreDto bookStoreDto) {
        return ResponseEntity.ok(bookStoreService.updateBookStore(bookStoreDto));
    }

    @ApiOperation(value = "서점 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Message> deleteBookStore(@PathVariable Long id) {
        bookStoreService.removeBookStore(id);
        return ResponseEntity.ok(Message.of("서점 삭제"));
    }

    @ApiOperation(value = "조건에 맞는 서점들 조회")
    @PostMapping("/search")
    public ResponseEntity<BookStorePageDto> findBookStoreByCriteria(@RequestBody BookStoreSearchDto bookStoreSearchDto) {
        return ResponseEntity.ok(bookStoreService.searchBookStoreList(bookStoreSearchDto));
    }

    @ApiOperation(value = "선택된 서점 삭제")
    @DeleteMapping("/list")
    public ResponseEntity<Message> deleteBookStoreList(@RequestBody BookStoreListDto list) {
        return ResponseEntity.ok(bookStoreService.deleteBookStoreList(list));
    }
}
