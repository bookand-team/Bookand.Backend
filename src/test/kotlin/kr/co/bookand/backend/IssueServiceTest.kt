package kr.co.bookand.backend

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.account.domain.AccountStatus
import kr.co.bookand.backend.account.domain.Role
import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.issue.domain.Issue
import kr.co.bookand.backend.issue.domain.IssueImage
import kr.co.bookand.backend.issue.domain.dto.CreateIssueRequest
import kr.co.bookand.backend.issue.repository.IssueImageRepository
import kr.co.bookand.backend.issue.repository.IssueRepository
import kr.co.bookand.backend.issue.service.IssueService
import kr.co.bookand.backend.util.s3.dto.FileDto
import kr.co.bookand.backend.util.s3.service.AwsS3Service
import java.time.LocalDateTime
import java.util.*

class IssueServiceTest : BehaviorSpec({
    val issueRepository = mockk<IssueRepository>()
    val issueImageRepository = mockk<IssueImageRepository>()
    val awsS3Service = mockk<AwsS3Service>()
    val issueService = IssueService(
        issueRepository,
        issueImageRepository,
        awsS3Service
    )

    Given("IssueService") {

        val account = Account(
            1L,
            "email",
            "password",
            "name",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Role.ADMIN,
            AccountStatus.NORMAL
        )

        val issue = Issue(
            1L,
            LocalDateTime.parse("2021-01-01T00:00:00"),
            "issueContent",
            "issueReportResponseEmail",
            true,
            "logFilePath",
            "deviceOS",
            1L,
            false,
            DeviceOSFilter.ALL,
            mutableListOf()
        )

        val issueImage = IssueImage(
            1L,
            "issueImage",
            issue
        )

        val createIssueRequest = CreateIssueRequest(
            issuedAt = "2021-01-01T00:00:00",
            issueContent = "issueContent",
            issueReportResponseEmail = "issueReportResponseEmail",
            issueImages = mutableListOf(),
            sendLogs = true,
            logFile = null,
            deviceOS = DeviceOSFilter.ALL
        )

        val fileDto = FileDto(
            url = "url",
            fileName = "fileName"
        )


        When("createIssue") {
            every { issueImageRepository.save(any()) } returns issueImage
            every { issueRepository.save(any()) } returns issue
            every { awsS3Service.uploadV2(any(), any(), any()) } returns fileDto

            val result = issueService.createIssue(account, createIssueRequest)

            Then("result") {
                result.id shouldBe 1L
            }
        }

        When("checkConfirmed") {
            every { issueRepository.findById(any()) } returns Optional.of(issue)

            val result = issueService.checkConfirmed(1L, true)

            Then("result") {
                result.statusCode shouldBe 200
            }
        }
    }
})