package kr.co.bookand.backend.feedback.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.feedback.dto.CreateFeedbackRequest
import kr.co.bookand.backend.feedback.dto.FeedbackIdResponse
import kr.co.bookand.backend.feedback.dto.FeedbackListResponse
import kr.co.bookand.backend.feedback.dto.FeedbackResponse
import kr.co.bookand.backend.feedback.service.FeedbackService
import kr.co.bookand.backend.issue.dto.IssueIdResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/feedbacks")
@Api(tags = ["피드백 API"])
class FeedbackController(
    private val feedbackService: FeedbackService,
    private val accountService : AccountService
){
    @ApiOperation(value = "피드백 생성")
    @PostMapping
    fun createFeedback(
        @RequestBody feedbackRequest: CreateFeedbackRequest
    ): ResponseEntity<FeedbackIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(feedbackService.createFeedback(account, feedbackRequest))
    }

    @ApiOperation(value = "피드백 리스트 조회 (WEB)")
    @GetMapping
    fun getFeedbackList(
        @PageableDefault pageable: Pageable
    ): ResponseEntity<FeedbackListResponse> {
        return ResponseEntity.ok(feedbackService.getFeedbackList(pageable))
    }

    @ApiOperation(value = "피드백 상세 정보 조회 (WEB)")
    @GetMapping("{feedbackId}")
    fun getFeedbackDetail(
        @PathVariable feedbackId: Long
    ): ResponseEntity<FeedbackResponse> {
        return ResponseEntity.ok(feedbackService.getFeedback(feedbackId))
    }

    @ApiOperation(value = "피드백 확인 여부 체크 하기")
    @PutMapping("/{feedbackId}")
    fun checkFeedback(
        @PathVariable feedbackId: Long,
        @RequestParam checkConfirmed: Boolean
    ): ResponseEntity<FeedbackIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(feedbackService.updateConfirmed(account, feedbackId, checkConfirmed))
    }

}