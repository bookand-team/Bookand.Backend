package kr.co.bookand.backend.feedback.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.feedback.domain.dto.KotlinCreateFeedbackRequest
import kr.co.bookand.backend.feedback.domain.dto.KotlinFeedbackIdResponse
import kr.co.bookand.backend.feedback.domain.dto.KotlinFeedbackListResponse
import kr.co.bookand.backend.feedback.domain.dto.KotlinFeedbackResponse
import kr.co.bookand.backend.feedback.service.KotlinFeedbackService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v3/feedbacks")
@Api(tags = ["피드백 API"])
class KotlinFeedbackController(
    private val feedbackService: KotlinFeedbackService,
    private val accountService : KotlinAccountService
){
    @ApiOperation(value = "피드백 생성")
    @PostMapping
    fun createFeedback(
        @RequestBody feedbackRequest: KotlinCreateFeedbackRequest
    ): ResponseEntity<KotlinFeedbackIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(feedbackService.createFeedback(account, feedbackRequest))
    }

    @ApiOperation(value = "피드백 리스트 조회 (WEB)")
    @GetMapping
    fun getFeedbackList(
        @PageableDefault pageable: Pageable?
    ): ResponseEntity<KotlinFeedbackListResponse> {
        return ResponseEntity.ok(feedbackService.getFeedbackList(pageable))
    }

    @ApiOperation(value = "피드백 상세 정보 조회 (WEB)")
    @GetMapping("{feedbackId}")
    fun getFeedbackDetail(
        @PathVariable feedbackId: Long
    ): ResponseEntity<KotlinFeedbackResponse> {
        return ResponseEntity.ok(feedbackService.getFeedback(feedbackId))
    }

}