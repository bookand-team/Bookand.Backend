package kr.co.bookand.backend.issue.domain.dto

import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.issue.domain.Issue
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class CreateIssueRequest(
    val issuedAt : String,
    val issueContent: String,
    val issueReportResponseEmail: String,
    val issueImages : List<String>,
    val sendLogs : Boolean,
    val logFile: MultipartFile?,
    val deviceOS: DeviceOSFilter
)

data class IssueIdResponse(
    val id: Long
)

data class IssueSimpleResponse(
    val id: Long,
    val issueReportResponseEmail: String,
    val accountId: Long,
    val issueContent: String,
    val issuedAt: LocalDateTime,
    val createdAt: String,
    val checkConfirmed: Boolean
){
    constructor(issue: Issue): this(
        id = issue.id,
        issueReportResponseEmail = issue.issueReportResponseEmail,
        accountId = issue.accountId,
        issueContent = issue.issueContent,
        issuedAt = issue.issuedAt,
        createdAt = issue.createdAt.toString(),
        checkConfirmed = issue.checkConfirmed
    )
}
data class IssueSimpleListResponse(
    val data : PageResponse<IssueSimpleResponse>,
)

data class IssueResponse(
    val id: Long,
    val issueReportResponseEmail: String,
    val deviceOS: DeviceOSFilter,
    val createdAt: String,
    val logFilePath: String?,
    val issuedAt: LocalDateTime,
    val issueContent: String,
    val issueImages: List<String>,
){
    constructor(issue: Issue): this(
        id = issue.id,
        issueReportResponseEmail = issue.issueReportResponseEmail,
        deviceOS = issue.deviceOS,
        createdAt = issue.createdAt.toString(),
        logFilePath = issue.logFilePath,
        issuedAt = issue.issuedAt,
        issueContent = issue.issueContent,
        issueImages = issue.issueImageList.map { it.imageUrl }
    )
}