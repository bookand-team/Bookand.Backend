package kr.co.bookand.backend.notification.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import kr.co.bookand.backend.notification.domain.dto.NotificationDto.*;
import kr.co.bookand.backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Api(tags = "공지사항 API")
public class NotificationController {

    private final NotificationService notificationService;

    @ApiOperation(value = "공지사항 생성")
    @PostMapping()
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody CreateNotificationRequest createNotificationRequest) {
        return ResponseEntity.ok(notificationService.createNotification(createNotificationRequest));
    }

    @ApiOperation(value = "공지사항 목록 조회")
    @Operation(summary = "공지사항 목록 조회", description = "커서 기반으로 되어 있습니다. " +
            "\n 초기에는 cursorId를 0 넣으시거나 요청 안하시면 됩니다.")
    @GetMapping()
    public ResponseEntity<PageResponse<NotificationResponse>> getNotificationList(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        return ResponseEntity.ok(notificationService.getNotificationList(pageable, cursorId));
    }

    @Deprecated
    @ApiOperation(value = "공지사항 상세 조회")
    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> getNotification(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.getNotification(notificationId));
    }
}
