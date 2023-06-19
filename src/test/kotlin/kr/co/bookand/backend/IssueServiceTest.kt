package kr.co.bookand.backend

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.account.domain.KotlinAccountStatus
import kr.co.bookand.backend.account.domain.KotlinRole
import kr.co.bookand.backend.common.KotlinDeviceOSFilter
import kr.co.bookand.backend.issue.domain.KotlinIssue
import kr.co.bookand.backend.issue.domain.KotlinIssueImage
import kr.co.bookand.backend.issue.domain.dto.KotlinCreateIssueRequest
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
    val awsS3Service = mockk<KotlinAwsS3Service>()
    val issueService = KotlinIssueService(
        issueRepository,
        issueImageRepository,
        awsS3Service
    )

    Given("IssueService") {

        val account = KotlinAccount(
            1L,
            "email",
            "password",
            "name",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            KotlinRole.ADMIN,
            KotlinAccountStatus.NORMAL
        )

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
            KotlinDeviceOSFilter.ALL,
            mutableListOf()
        )

        val issueImage = KotlinIssueImage(
            1L,
            "issueImage",
            issue
        )

        val createIssueRequest = KotlinCreateIssueRequest(
            issuedAt = "2021-01-01T00:00:00",
            issueContent = "issueContent",
            issueReportResponseEmail = "issueReportResponseEmail",
            issueImages = mutableListOf(),
            sendLogs = true,
            logFile = null,
            deviceOS = KotlinDeviceOSFilter.ALL
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