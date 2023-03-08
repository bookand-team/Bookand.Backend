package kr.co.bookand.backend.feedback.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import kr.co.bookand.backend.feedback.domain.dto.FeedbackDto.*;
import kr.co.bookand.backend.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
@Api(tags = "피드백 API")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @ApiOperation(value = "피드백 생성")
    @PostMapping("")
    public ResponseEntity<FeedbackResponse> createFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        return ResponseEntity.ok(feedbackService.createFeedback(feedbackRequest));
    }

    @ApiOperation(value = "피드백 리스트 조회")
    @GetMapping("")
    public ResponseEntity<PageResponse<FeedbackListResponse>> getFeedbackList(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(feedbackService.getFeedbackList(pageable));
    }

    @ApiOperation(value = "피드백 상세 정보 조회")
    @GetMapping("{feedbackId}")
    public ResponseEntity<FeedbackDetailResponse> getFeedbackDetail(@PathVariable Long feedbackId) {
        return ResponseEntity.ok(feedbackService.getFeedbackDetail(feedbackId));
    }
}
