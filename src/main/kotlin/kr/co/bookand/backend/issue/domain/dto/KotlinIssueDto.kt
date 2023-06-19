package kr.co.bookand.backend.issue.domain.dto

import kr.co.bookand.backend.common.KotlinDeviceOSFilter
import kr.co.bookand.backend.common.KotlinPageResponse
import kr.co.bookand.backend.issue.domain.KotlinIssue
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class KotlinCreateIssueRequest(
    val issuedAt : String,
    val issueContent: String,
    val issueReportResponseEmail: String,
    val issueImages : List<String>,
    val sendLogs : Boolean,
    val logFile: MultipartFile?,
    val deviceOS: KotlinDeviceOSFilter
)

data class KotlinIssueIdResponse(
    val id: Long
)

data class KotlinIssueSimpleResponse(
    val id: Long,
    val issueReportResponseEmail: String,
    val accountId: Long,
    val issueContent: String,
    val issuedAt: LocalDateTime,
    val createdAt: String,
    val checkConfirmed: Boolean
){
    constructor(issue: KotlinIssue): this(
        id = issue.id,
        issueReportResponseEmail = issue.issueReportResponseEmail,
        accountId = issue.accountId,
        issueContent = issue.issueContent,
        issuedAt = issue.issuedAt,
        createdAt = issue.createdAt.toString(),
        checkConfirmed = issue.checkConfirmed
    )
}
data class KotlinIssueSimpleListResponse(
    val data : KotlinPageResponse<KotlinIssueSimpleResponse>,
)

data class KotlinIssueResponse(
    val id: Long,
    val issueReportResponseEmail: String,
    val deviceOS: KotlinDeviceOSFilter,
    val createdAt: String,
    val logFilePath: String?,
    val issuedAt: LocalDateTime,
    val issueContent: String,
    val issueImages: List<String>,
){
    constructor(issue: KotlinIssue): this(
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