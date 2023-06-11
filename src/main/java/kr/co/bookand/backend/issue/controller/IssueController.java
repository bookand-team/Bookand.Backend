package kr.co.bookand.backend.issue.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.common.domain.Message;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import kr.co.bookand.backend.issue.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.bookand.backend.issue.domain.dto.IssueDto.*;

@RestController
@RequestMapping("/api/v1/issues")
@RequiredArgsConstructor
@Api(tags = "오류 및 버그 API")
public class IssueController {
    private final IssueService issueService;

    @ApiOperation(value = "오류 및 버그 신고 생성")
    @PostMapping("")
    public ResponseEntity<IssueIdResponse> createIssue(
            @ModelAttribute CreateIssueRequest createIssueRequest
    ) {
        return ResponseEntity.ok(issueService.createIssue(createIssueRequest));
    }

    @ApiOperation(value = "오류 및 버그 신고 조회 (WEB)")
    @GetMapping("")
    public ResponseEntity<PageResponse<IssueSimpleResponse>> getIssueList(
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(issueService.getIssueList(pageable));
    }

    @ApiOperation(value = "오류 및 버그 신고 상세 조회")
    @GetMapping("/{issueId}")
    public ResponseEntity<IssueResponse> getIssue(
            @PathVariable Long issueId
    ) {
        return ResponseEntity.ok(issueService.getIssue(issueId));
    }

    @ApiOperation(value = "오류 및 버그 신고 체크 하기")
    @PutMapping("/{issueId}/check")
    public ResponseEntity<Message> checkIssue(
            @PathVariable Long issueId,
            @RequestParam Boolean checkConfirmed
    ) {
        return ResponseEntity.ok(issueService.checkConfirmed(issueId, checkConfirmed));
    }

}
