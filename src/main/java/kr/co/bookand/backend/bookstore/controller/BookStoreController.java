package kr.co.bookand.backend.bookstore.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.bookstore.service.BookStoreService;
import kr.co.bookand.backend.common.domain.Message;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto.*;

@RestController
@RequestMapping("/api/v1/bookstores")
@RequiredArgsConstructor
@Api(tags = "서점 API")
public class BookStoreController {

    private final BookStoreService bookStoreService;

    @ApiOperation(value = "서점 등록")
    @PostMapping("")
    public ResponseEntity<BookStoreResponse> createBookStore(@RequestBody BookStoreRequest bookStoreDto) {
        return ResponseEntity.ok(bookStoreService.createBookStore(bookStoreDto));
    }

    @ApiOperation(value = "서점 상세 조회 (APP)")
    @GetMapping("/{id}")
    public ResponseEntity<BookStoreResponse> findBookStore(@PathVariable Long id) {
        return ResponseEntity.ok(bookStoreService.getBookStore(id));
    }

    @ApiOperation(value = "서점 전체 조회 (APP)")
    @GetMapping("")
    public ResponseEntity<BookStorePageResponseApp> getBookStoreListApp(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(bookStoreService.getBookStoreListApp(pageable));
    }

    @ApiOperation(value = "서점 조건 조회")
    @GetMapping("/search")
    public ResponseEntity<BookStorePageResponse> searchBookStoreList(
            @RequestParam("search") String search,
            @RequestParam("theme") String theme,
            @RequestParam("status") String status,
            @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(bookStoreService.searchBookStoreList(search, theme, status, pageable));
    }

    @ApiOperation(value = "서점 전체 조회 (WEB)")
    @GetMapping("/web")
    public ResponseEntity<BookStorePageResponse> getBookStoreList(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(bookStoreService.getBookStoreList(pageable));
    }

    @ApiOperation(value = "서점 수정")
    @PutMapping("/{id}")
    public ResponseEntity<BookStoreWebResponse> updateBookStore(@PathVariable Long id, @RequestBody BookStoreRequest bookStoreRequest) {
        return ResponseEntity.ok(bookStoreService.updateBookStore(id, bookStoreRequest));
    }

    @ApiOperation(value = "서점 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Message> deleteBookStore(@PathVariable Long id) {
        bookStoreService.deleteBookStore(id);
        return ResponseEntity.ok(Message.of("서점 삭제"));
    }

    @ApiOperation(value = "선택된 서점 삭제")
    @DeleteMapping("/list")
    public ResponseEntity<Message> deleteBookStoreList(@RequestBody BookStoreListRequest list) {
        return ResponseEntity.ok(bookStoreService.deleteBookStoreList(list));
    }

    @ApiOperation(value = "서점 보기 변경")
    @PutMapping("/{id}/status")
    public ResponseEntity<BookStoreWebResponse> updateBookStoreStatus(@PathVariable Long id) {
        return ResponseEntity.ok(bookStoreService.updateBookStoreStatus(id));
    }

    @ApiOperation(value = "새로운 서점 제보")
    @PostMapping("/report")
    public ResponseEntity<Message> reportBookStore(@RequestBody ReportBookStoreRequest reportBookStoreRequest) {
        return ResponseEntity.ok(bookStoreService.reportBookStore(reportBookStoreRequest));
    }

    @ApiOperation(value = "서점 제보 답변")
    @PutMapping("/report/{reportId}/answer")
    public ResponseEntity<Message> answerReportBookStore(@PathVariable Long reportId, @RequestBody AnswerReportRequest answerReportRequest) {
        return ResponseEntity.ok(bookStoreService.answerReportBookStore(reportId, answerReportRequest));
    }

    @ApiOperation(value = "서점 제보 전체 조회")
    @GetMapping("/report")
    public ResponseEntity<PageResponse<BookStoreReportList>> getBookStoreReportList(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(bookStoreService.getBookStoreReportList(pageable));
    }
}
