package kr.co.bookand.backend.issue.service

import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.common.domain.Message
import kr.co.bookand.backend.issue.domain.Issue
import kr.co.bookand.backend.issue.domain.KotlinIssue
import kr.co.bookand.backend.issue.domain.KotlinIssueImage
import kr.co.bookand.backend.issue.domain.dto.CreateIssueRequest
import kr.co.bookand.backend.issue.domain.dto.IssueIdResponse
import kr.co.bookand.backend.issue.repository.KotlinIssueImageRepository
import kr.co.bookand.backend.issue.repository.KotlinIssueRepository
import kr.co.bookand.backend.util.s3.dto.FileDto
import kr.co.bookand.backend.util.s3.service.KotlinAwsS3Service
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
class KotlinIssueService(
    private val kotlinIssueRepository: KotlinIssueRepository,
    private val kotlinIssueImageRepository: KotlinIssueImageRepository,
    private val kotlinAccountService: KotlinAccountService,
    private val kotlinAwsS3Service: KotlinAwsS3Service
) {
    @Transactional
    fun createIssue(createIssueRequest: CreateIssueRequest): IssueIdResponse {
        //val currentAccountEmail = "SecurityUtil.getCurrentAccountEmail()"
        val issueImagesList = createIssueRequest.issueImages

        var fileDto = FileDto("", "")
        if (createIssueRequest.logFile != null) {
            val multipartFile = createIssueRequest.logFile
            fileDto = kotlinAwsS3Service.uploadV2(multipartFile, "reportLog", "loginAccount")
        }
        val issue = KotlinIssue(createIssueRequest, 1L)

        if (createIssueRequest.sendLogs) {
            issue.setLogFile(fileDto.url, fileDto.fileName)
        }

        val saveIssue: KotlinIssue = kotlinIssueRepository.save(issue)
        val issueImageList = issueImagesList
            .map { issueImage: String ->
                val image = KotlinIssueImage(issueImage, saveIssue)
                val saveIssueImage: KotlinIssueImage = kotlinIssueImageRepository.save(image)
                saveIssueImage
            }
            .toList()
        issue.setIssueImage(issueImageList)

        return IssueIdResponse(saveIssue.id)
    }

    @Transactional
    fun checkConfirmed(issueId: Long, checkConfirmed: Boolean): IssueIdResponse {
        val issue: KotlinIssue = kotlinIssueRepository.findById(issueId)
            .orElseThrow { throw RuntimeException("ErrorCode.ISSUE_NOT_FOUND") }
        issue.checkConfirmed(checkConfirmed)
        return IssueIdResponse(issue.id)
    }
}