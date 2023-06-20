package kr.co.bookand.backend.bookstore.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.bookstore.domain.Bookstore
import kr.co.bookand.backend.bookstore.domain.dto.*
import kr.co.bookand.backend.bookstore.service.BookstoreService
import kr.co.bookand.backend.common.domain.MessageResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/bookstores")
@Api(tags = ["서점 API"])
class BookstoreController(
    private val bookstoreService: BookstoreService,
    private val accountService: AccountService
) {
    @ApiOperation(value = "서점 등록")
    @PostMapping("")
    fun createBookstore(
        @RequestBody bookstoreRequest: BookstoreRequest
    ): ResponseEntity<BookstoreIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookstoreService.createBookstore(account, bookstoreRequest))
    }

    @ApiOperation(value = "서점 상세 조회 (APP)")
    @GetMapping("/{id}")
    fun findBookstore(
        @PathVariable id: Long
    ): ResponseEntity<BookstoreResponse> {
        return ResponseEntity.ok(bookstoreService.getBookstoreResponse(id))
    }

    @ApiOperation(value = "서점 전체 조회 (APP)")
    @GetMapping("")
    fun getBookstoreListApp(
        @PageableDefault(sort = ["modifiedAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<BookstorePageResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookstoreService.getBookstoreSimpleList(account, pageable))
    }

    @ApiOperation(value = "서점 조건 조회")
    @GetMapping("/search")
    fun searchBookstoreList(
        @RequestParam("search") search: String?,
        @RequestParam("theme") theme: String?,
        @RequestParam("status") status: String?,
        @PageableDefault(sort = ["modifiedAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<BookstoreWebPageResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookstoreService.searchBookstoreList(account, search, theme, status, pageable))
    }

    @ApiOperation(value = "서점 전체 조회 (WEB)")
    @GetMapping("/web")
    fun getBookstoreList(
        @PageableDefault(sort = ["modifiedAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<BookstoreWebPageResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookstoreService.getWebBookstoreList(account, pageable))
    }

    @ApiOperation(value = "서점 수정")
    @PutMapping("/{id}")
    fun updateBookstore(
        @PathVariable id: Long,
        @RequestBody bookStoreRequest: BookstoreRequest
    ): ResponseEntity<BookstoreWebResponse> {
        return ResponseEntity.ok(bookstoreService.updateBookstore(id, bookStoreRequest))
    }

    @ApiOperation(value = "서점 삭제")
    @DeleteMapping("/{id}")
    fun deleteBookstore(
        @PathVariable id: Long
    ): ResponseEntity<MessageResponse> {
        return ResponseEntity.ok(bookstoreService.deleteBookstore(id))
    }

    @ApiOperation(value = "선택된 서점 삭제")
    @DeleteMapping("/list")
    fun deleteBookstoreList(
        @RequestBody list: BookstoreListRequest
    ): ResponseEntity<MessageResponse> {
        return ResponseEntity.ok(bookstoreService.deleteBookstoreList(list))
    }

    @ApiOperation(value = "서점 보기 변경")
    @PutMapping("/{id}/status")
    fun updateBookstoreStatus(
        @PathVariable id: Long
    ): ResponseEntity<MessageResponse> {
        return ResponseEntity.ok(bookstoreService.updateBookstoreStatus(id))
    }

    @ApiOperation(value = "새로운 서점 제보")
    @PostMapping("/report")
    fun reportBookstore(
        @RequestBody reportBookstoreRequest: ReportBookstoreRequest
    ): ResponseEntity<ReportBookstoreIdResponse> {
        return ResponseEntity.ok(bookstoreService.createReportBookstore(reportBookstoreRequest))
    }

    @ApiOperation(value = "서점 제보 답변")
    @PutMapping("/report/{reportId}/answer")
    fun answerReportBookstore(
        @PathVariable reportId: Long,
        @RequestBody answerReportRequest:AnswerReportRequest
    ): ResponseEntity<MessageResponse> {
        return ResponseEntity.ok(bookstoreService.answerReportBookstore(reportId, answerReportRequest))
    }

    @ApiOperation(value = "서점 제보 전체 조회")
    @GetMapping("/report")
    fun getBookstoreReportList(
        @PageableDefault pageable: Pageable
    ): ResponseEntity<ReportBookstoreListResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookstoreService.getBookstoreReportList(pageable, account))
    }

    @ApiOperation(value = "서점 지도 정보 조회")
    @GetMapping("/address")
    fun getBookstoreAddress(): ResponseEntity<BookstoreAddressListResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookstoreService.getBookstoreAddressList(account))
    }
}