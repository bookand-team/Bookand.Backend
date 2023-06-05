package kr.co.bookand.backend.notice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import kr.co.bookand.backend.notice.domain.dto.NoticeDto.*;
import kr.co.bookand.backend.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Api(tags = "공지사항 API")
public class NoticeController {

    private final NoticeService noticeService;

    @ApiOperation(value = "공지사항 생성")
    @PostMapping()
    public ResponseEntity<NoticeIdResponse> createNotice(@RequestBody CreateNoticeRequest createNoticeRequest) {
        return ResponseEntity.ok(noticeService.createNotice(createNoticeRequest));
    }

    @ApiOperation(value = "공지사항 목록 조회")
    @Operation(summary = "공지사항 목록 조회", description = "커서 기반으로 되어 있습니다. " +
            "\n 초기에는 cursorId를 0 넣으시거나 요청 안하시면 됩니다.")
    @GetMapping()
    public ResponseEntity<PageResponse<NoticeResponse>> getNoticeSimpleList(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        return ResponseEntity.ok(noticeService.getNoticeSimpleList(pageable, cursorId));
    }

    @ApiOperation(value = "공지사항 목록 조회")
    @GetMapping("/web")
    public ResponseEntity<PageResponse<NoticeWebResponse>> getNoticeList(
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(noticeService.getNoticeList(pageable));
    }

    @Deprecated
    @ApiOperation(value = "공지사항 상세 조회")
    @GetMapping("/{notificationId}")
    public ResponseEntity<NoticeResponse> getNotice(@PathVariable Long notificationId) {
        return ResponseEntity.ok(noticeService.getNotice(notificationId));
    }

    @ApiOperation(value = "공지사항 수정")
    @PutMapping("/{notificationId}")
    public ResponseEntity<NoticeIdResponse> updateNotice(@PathVariable Long notificationId, @RequestBody UpdateNoticeRequest updateNoticeRequest) {
        return ResponseEntity.ok(noticeService.updateNotice(notificationId, updateNoticeRequest));
    }

    @ApiOperation(value = "공지사항 삭제")
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<NoticeMessage> deleteNotice(@PathVariable Long notificationId) {
        return ResponseEntity.ok(noticeService.deleteNotice(notificationId));
    }

    @ApiOperation(value = "공지사항 상태 변경")
    @PutMapping("/{notificationId}/status")
    public ResponseEntity<NoticeMessage> updateNoticeStatus(@PathVariable Long notificationId) {
        return ResponseEntity.ok(noticeService.updateNoticeStatus(notificationId));
    }
}
