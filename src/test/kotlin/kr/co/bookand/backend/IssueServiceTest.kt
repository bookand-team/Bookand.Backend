package kr.co.bookand.backend

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.common.domain.DeviceOSFilter
import kr.co.bookand.backend.issue.domain.KotlinIssue
import kr.co.bookand.backend.issue.domain.KotlinIssueImage
import kr.co.bookand.backend.issue.domain.dto.CreateIssueRequest
import kr.co.bookand.backend.issue.repository.KotlinIssueImageRepository
import kr.co.bookand.backend.issue.repository.KotlinIssueRepository
import kr.co.bookand.backend.issue.service.KotlinIssueService
import kr.co.bookand.backend.util.s3.dto.FileDto
import kr.co.bookand.backend.util.s3.service.KotlinAwsS3Service
import java.time.LocalDateTime
import java.util.*

class IssueServiceTest : BehaviorSpec({
    val issueRepository = mockk<KotlinIssueRepository>()
    val issueImageRepository = mockk<KotlinIssueImageRepository>()
    val accountService = mockk<KotlinAccountService>()
    val awsS3Service = mockk<KotlinAwsS3Service>()
    val issueService = KotlinIssueService(
        issueRepository,
        issueImageRepository,
        accountService,
        awsS3Service
    )

    Given("IssueService") {

        val issue = KotlinIssue(
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

        val issueImage = KotlinIssueImage(
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

            val result = issueService.createIssue(createIssueRequest)

            Then("result") {
                result.id shouldBe 1L
            }
        }

        When("checkConfirmed") {
            every { issueRepository.findById(any()) } returns Optional.of(issue)

            val result = issueService.checkConfirmed(1L, true)

            Then("result") {
                result.id shouldBe 1L
            }
        }
    }
})