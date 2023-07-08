package kr.co.bookand.backend.issue.dto

import io.swagger.annotations.ApiModelProperty
import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.issue.model.Issue
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class CreateIssueRequest(
    @ApiModelProperty(value = "오류 및 버그 신고 날짜 yyyy-MM-dd'T'HH:mm:ss")
    val issuedAt : String,
    @ApiModelProperty(value = "오류 및 버그 신고 내용")
    val issueContent: String,
    @ApiModelProperty(value = "오류 및 버그 신고 답변 이메일")
    val issueReportResponseEmail: String,
    @ApiModelProperty(value = "오류 및 버그 신고 이미지")
    val issueImages : List<String>,
    @ApiModelProperty(value = "로그 전송 여부")
    val sendLogs : Boolean,
    @ApiModelProperty(value = "로그 파일")
    val logFile: MultipartFile?,
    @ApiModelProperty(value = "유저 운영체제 IOS, ANDROID")
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
    val isConfirmed: Boolean
){
    constructor(issue: Issue): this(
        id = issue.id,
        issueReportResponseEmail = issue.issueReportResponseEmail,
        accountId = issue.accountId,
        issueContent = issue.issueContent,
        issuedAt = issue.issuedAt,
        createdAt = issue.createdAt.toString(),
        isConfirmed = issue.isConfirmed
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
    val isConfirmed: Boolean
){
    constructor(issue: Issue): this(
        id = issue.id,
        issueReportResponseEmail = issue.issueReportResponseEmail,
        deviceOS = issue.deviceOS,
        createdAt = issue.createdAt.toString(),
        logFilePath = issue.logFilePath,
        issuedAt = issue.issuedAt,
        issueContent = issue.issueContent,
        issueImages = issue.issueImageList.map { it.imageUrl },
        isConfirmed = issue.isConfirmed
    )
}