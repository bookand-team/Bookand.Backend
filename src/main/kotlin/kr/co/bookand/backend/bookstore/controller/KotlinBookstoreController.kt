package kr.co.bookand.backend.bookstore.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import kr.co.bookand.backend.bookstore.domain.dto.*
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto.*
import kr.co.bookand.backend.bookstore.service.KotlinBookstoreService
import kr.co.bookand.backend.common.domain.KotlinMessageResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v3/bookstore")
@Api(tags = ["서점 API"])
class KotlinBookstoreController(
    private val bookstoreService: KotlinBookstoreService,
    private val accountService: KotlinAccountService
) {
    @ApiOperation(value = "서점 등록")
    @PostMapping("")
    fun createBookStore(
        @RequestBody bookstoreRequest: KotlinBookstoreRequest
    ): ResponseEntity<KotlinBookstoreIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookstoreService.createBookStore(account, bookstoreRequest))
    }

    @ApiOperation(value = "서점 상세 조회 (APP)")
    @GetMapping("/{id}")
    fun findBookStore(
        @PathVariable id: Long
    ): ResponseEntity<KotlinBookstore> {
        return ResponseEntity.ok(bookstoreService.getBookstore(id))
    }

    @ApiOperation(value = "서점 전체 조회 (APP)")
    @GetMapping("")
    fun getBookStoreListApp(
        @PageableDefault(sort = ["modifiedAt"], direction = Sort.Direction.DESC) pageable: Pageable?
    ): ResponseEntity<KotlinBookstorePageResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookstoreService.getBookstoreSimpleList(account, pageable))
    }

    @ApiOperation(value = "서점 조건 조회")
    @GetMapping("/search")
    fun searchBookStoreList(
        @RequestParam("search") search: String?,
        @RequestParam("theme") theme: String?,
        @RequestParam("status") status: String?,
        @PageableDefault(sort = ["modifiedAt"], direction = Sort.Direction.DESC) pageable: Pageable?
    ): ResponseEntity<KotlinWebBookstorePageResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookstoreService.searchBookstoreList(account, search, theme, status, pageable))
    }

    @ApiOperation(value = "서점 전체 조회 (WEB)")
    @GetMapping("/web")
    fun getBookStoreList(
        @PageableDefault(sort = ["modifiedAt"], direction = Sort.Direction.DESC) pageable: Pageable?
    ): ResponseEntity<KotlinWebBookstorePageResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookstoreService.getWebBookstoreList(account, pageable))
    }

    @ApiOperation(value = "서점 수정")
    @PutMapping("/{id}")
    fun updateBookStore(
        @PathVariable id: Long,
        @RequestBody bookStoreRequest: KotlinBookstoreRequest
    ): ResponseEntity<KotlinWebBookstoreResponse> {
        return ResponseEntity.ok(bookstoreService.updateBookStore(id, bookStoreRequest))
    }

    @ApiOperation(value = "서점 삭제")
    @DeleteMapping("/{id}")
    fun deleteBookStore(
        @PathVariable id: Long
    ): ResponseEntity<KotlinMessageResponse> {
        return ResponseEntity.ok(bookstoreService.deleteBookStore(id))
    }

    @ApiOperation(value = "선택된 서점 삭제")
    @DeleteMapping("/list")
    fun deleteBookStoreList(
        @RequestBody list: KotlinBookstoreListRequest
    ): ResponseEntity<KotlinMessageResponse> {
        return ResponseEntity.ok(bookstoreService.deleteBookStoreList(list))
    }

    @ApiOperation(value = "서점 보기 변경")
    @PutMapping("/{id}/status")
    fun updateBookStoreStatus(
        @PathVariable id: Long
    ): ResponseEntity<KotlinMessageResponse> {
        return ResponseEntity.ok(bookstoreService.updateBookstoreStatus(id))
    }

    @ApiOperation(value = "새로운 서점 제보")
    @PostMapping("/report")
    fun reportBookStore(
        @RequestBody reportBookStoreRequest: KotlinReportBookstoreRequest
    ): ResponseEntity<KotlinReportBookstoreIdResponse> {
        return ResponseEntity.ok(bookstoreService.createReportBookstore(reportBookStoreRequest))
    }

    @ApiOperation(value = "서점 제보 답변")
    @PutMapping("/report/{reportId}/answer")
    fun answerReportBookStore(
        @PathVariable reportId: Long,
        @RequestBody answerReportRequest:KotlinAnswerReportRequest
    ): ResponseEntity<KotlinMessageResponse> {
        return ResponseEntity.ok(bookstoreService.answerReportBookstore(reportId, answerReportRequest))
    }

    @ApiOperation(value = "서점 제보 전체 조회")
    @GetMapping("/report")
    fun getBookStoreReportList(
        @PageableDefault pageable: Pageable?
    ): ResponseEntity<KotlinReportBookstoreListResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookstoreService.getBookStoreReportList(pageable, account))
    }

    @ApiOperation(value = "서점 지도 정보 조회")
    @GetMapping("/address")
    fun getBookStoreAddress(): ResponseEntity<KotlinBookStoreAddressListResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookstoreService.getBookstoreAddressList(account))
    }
}