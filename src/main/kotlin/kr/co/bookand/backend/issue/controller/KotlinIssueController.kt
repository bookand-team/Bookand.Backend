package kr.co.bookand.backend.issue.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.common.domain.KotlinMessageResponse
import kr.co.bookand.backend.issue.domain.dto.KotlinCreateIssueRequest
import kr.co.bookand.backend.issue.domain.dto.KotlinIssueIdResponse
import kr.co.bookand.backend.issue.domain.dto.KotlinIssueResponse
import kr.co.bookand.backend.issue.domain.dto.KotlinIssueSimpleListResponse
import kr.co.bookand.backend.issue.service.KotlinIssueService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v3/issues")
@Api(tags = ["오류 및 버그 API"])
class KotlinIssueController(
    private val issueService: KotlinIssueService,
    private val accountService: KotlinAccountService
) {
    @ApiOperation(value = "오류 및 버그 신고 생성")
    @PostMapping("")
    fun createIssue(
        @ModelAttribute createIssueRequest: KotlinCreateIssueRequest
    ): ResponseEntity<KotlinIssueIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(issueService.createIssue(account, createIssueRequest))
    }

    @ApiOperation(value = "오류 및 버그 신고 조회 (WEB)")
    @GetMapping("")
    fun getIssueList(
        @PageableDefault pageable: Pageable?
    ): ResponseEntity<KotlinIssueSimpleListResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(issueService.getIssueList(account, pageable))
    }

    @ApiOperation(value = "오류 및 버그 신고 상세 조회")
    @GetMapping("/{issueId}")
    fun getIssue(
        @PathVariable issueId: Long
    ): ResponseEntity<KotlinIssueResponse> {
        return ResponseEntity.ok(issueService.getIssue(issueId))
    }

    @ApiOperation(value = "오류 및 버그 신고 체크 하기")
    @PutMapping("/{issueId}/check")
    fun checkIssue(
        @PathVariable issueId: Long,
        @RequestParam checkConfirmed: Boolean
    ): ResponseEntity<KotlinMessageResponse> {
        return ResponseEntity.ok(issueService.checkConfirmed(issueId, checkConfirmed))
    }
}