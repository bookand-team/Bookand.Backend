package kr.co.bookand.backend.issue.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.common.model.MessageResponse
import kr.co.bookand.backend.issue.dto.CreateIssueRequest
import kr.co.bookand.backend.issue.dto.IssueIdResponse
import kr.co.bookand.backend.issue.dto.IssueResponse
import kr.co.bookand.backend.issue.dto.IssueSimpleListResponse
import kr.co.bookand.backend.issue.service.IssueService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/issues")
@Api(tags = ["오류 및 버그 API"])
class IssueController(
    private val issueService: IssueService,
    private val accountService: AccountService
) {
    @ApiOperation(value = "오류 및 버그 신고 생성")
    @PostMapping("")
    fun createIssue(
        @ModelAttribute createIssueRequest: CreateIssueRequest
    ): ResponseEntity<IssueIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(issueService.createIssue(account, createIssueRequest))
    }

    @ApiOperation(value = "오류 및 버그 신고 조회 (WEB)")
    @GetMapping("")
    fun getIssueList(
        @PageableDefault pageable: Pageable
    ): ResponseEntity<IssueSimpleListResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(issueService.getIssueList(account, pageable))
    }

    @ApiOperation(value = "오류 및 버그 신고 상세 조회")
    @GetMapping("/{issueId}")
    fun getIssue(
        @PathVariable issueId: Long
    ): ResponseEntity<IssueResponse> {
        return ResponseEntity.ok(issueService.getIssue(issueId))
    }

    @ApiOperation(value = "오류 및 버그 신고 체크 하기")
    @PutMapping("/{issueId}/check")
    fun checkIssue(
        @PathVariable issueId: Long,
        @RequestParam checkConfirmed: Boolean
    ): ResponseEntity<IssueIdResponse> {
        return ResponseEntity.ok(issueService.checkConfirmed(issueId, checkConfirmed))
    }
}