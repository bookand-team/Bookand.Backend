package kr.co.bookand.backend.notice.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.Operation
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.common.model.MessageResponse
import kr.co.bookand.backend.notice.dto.*
import kr.co.bookand.backend.notice.service.NoticeService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/notifications")
@Api(tags = ["공지사항 API"])
class NoticeController(
    private val noticeService: NoticeService,
    private val accountService: AccountService
) {
    @ApiOperation(value = "공지사항 생성 (WEB)")
    @PostMapping
    fun createNotice(
        @RequestBody createNoticeRequest: CreateNoticeRequest
    ): ResponseEntity<NoticeIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(noticeService.createNotice(account, createNoticeRequest))
    }

    @ApiOperation(value = "공지사항 목록 조회")
    @Operation(
        summary = "공지사항 목록 조회",
        description = "커서 기반으로 되어 있습니다. 초기에는 cursorId를 0 넣으시거나 요청 안하시면 됩니다."
    )
    @GetMapping
    fun getNoticeSimpleList(
        @PageableDefault pageable: Pageable,
        @RequestParam(required = false) cursorId: Long?
    ): ResponseEntity<PageResponse<NoticeResponse>> {
        return ResponseEntity.ok(noticeService.getNoticeSimpleList(pageable, cursorId))
    }

    @ApiOperation(value = "공지사항 목록 조회 (WEB)")
    @GetMapping("/web")
    fun getNoticeList(
        @PageableDefault pageable: Pageable
    ): ResponseEntity<NoticePageResponse> {
        return ResponseEntity.ok(noticeService.getNoticeList(pageable))
    }

    @Deprecated("")
    @ApiOperation(value = "공지사항 상세 조회")
    @GetMapping("/{notificationId}")
    fun getNotice(@PathVariable notificationId: Long
    ): ResponseEntity<NoticeResponse> {
        return ResponseEntity.ok(noticeService.getNoticeDetail(notificationId))
    }

    @ApiOperation(value = "공지사항 수정 (WEB)")
    @PutMapping("/{notificationId}")
    fun updateNotice(
        @PathVariable notificationId: Long,
        @RequestBody updateNoticeRequest: UpdateNoticeRequest
    ): ResponseEntity<NoticeIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(noticeService.updateNotice(account, notificationId, updateNoticeRequest))
    }

    @ApiOperation(value = "공지사항 삭제 (WEB)")
    @DeleteMapping("/{notificationId}")
    fun deleteNotice(@PathVariable notificationId: Long
    ): ResponseEntity<MessageResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(noticeService.deleteNotice(account, notificationId))
    }

    @ApiOperation(value = "공지사항 상태 변경 (WEB)")
    @PutMapping("/{notificationId}/status")
    fun updateNoticeStatus(@PathVariable notificationId: Long
    ): ResponseEntity<NoticeIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(noticeService.updateNoticeStatus(account, notificationId))
    }
}