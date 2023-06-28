package kr.co.bookand.backend.issue.model

import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.model.BaseEntity
import kr.co.bookand.backend.issue.dto.CreateIssueRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.*

@Entity
class Issue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    var id: Long = 0,

    var issuedAt: LocalDateTime,
    var issueContent: String,
    var issueReportResponseEmail: String,
    var sendLogs: Boolean,
    var logFilePath: String?,
    var logFileName: String?,
    var accountId: Long,
    var isConfirmed: Boolean,
    @Enumerated(EnumType.STRING)
    var deviceOS: DeviceOSFilter,

    @OneToMany(mappedBy = "issue", cascade = [CascadeType.ALL], orphanRemoval = true)
    var issueImageList: MutableList<IssueImage> = mutableListOf()

) : BaseEntity() {

    constructor(createIssueRequest: CreateIssueRequest, accountId: Long) : this(

        issuedAt = LocalDateTime.parse(createIssueRequest.issuedAt, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
        issueContent = createIssueRequest.issueContent,
        issueReportResponseEmail = createIssueRequest.issueReportResponseEmail,
        sendLogs = createIssueRequest.sendLogs,
        logFilePath = null,
        logFileName = null,
        accountId = accountId,
        isConfirmed = false,
        deviceOS = createIssueRequest.deviceOS
    )

    fun setLogFile(logFilePath: String, logFileName: String) {
        this.logFilePath = logFilePath
        this.logFileName = logFileName
    }

    fun updateConfirmed(updateConfirmed: Boolean) {
        this.isConfirmed = updateConfirmed
    }

    fun setIssueImage(issueImage: List<IssueImage>) {
        issueImageList.addAll(issueImage)
    }

}