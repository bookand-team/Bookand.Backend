package kr.co.bookand.backend.issue.service

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.common.KotlinPageResponse
import kr.co.bookand.backend.common.domain.KotlinMessageResponse
import kr.co.bookand.backend.issue.domain.KotlinIssue
import kr.co.bookand.backend.issue.domain.KotlinIssueImage
import kr.co.bookand.backend.issue.domain.dto.*
import kr.co.bookand.backend.issue.repository.KotlinIssueImageRepository
import kr.co.bookand.backend.issue.repository.KotlinIssueRepository
import kr.co.bookand.backend.util.s3.dto.FileDto
import kr.co.bookand.backend.util.s3.service.KotlinAwsS3Service
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
class KotlinIssueService(
    private val kotlinIssueRepository: KotlinIssueRepository,
    private val kotlinIssueImageRepository: KotlinIssueImageRepository,
    private val kotlinAwsS3Service: KotlinAwsS3Service
) {

    fun getIssue(issueId: Long): KotlinIssueResponse {
        return kotlinIssueRepository.findById(issueId)
            .map { KotlinIssueResponse(it) }
            .orElseThrow { throw RuntimeException("ErrorCode.ISSUE_NOT_FOUND") }
    }

    fun getIssueList(currentAccount: KotlinAccount, pageable: Pageable?): KotlinIssueSimpleListResponse {
        currentAccount.role.checkAdminAndManager()
        val map = kotlinIssueRepository.findAll(pageable)
            .map { KotlinIssueSimpleResponse(it) }
        return KotlinIssueSimpleListResponse(KotlinPageResponse.of(map))
    }

    @Transactional
    fun createIssue(
        currentAccount: KotlinAccount,
        createIssueRequest: KotlinCreateIssueRequest
    ): KotlinIssueIdResponse {
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

        return KotlinIssueIdResponse(saveIssue.id)
    }

    @Transactional
    fun checkConfirmed(issueId: Long, checkConfirmed: Boolean): KotlinMessageResponse {
        val issue: KotlinIssue = kotlinIssueRepository.findById(issueId)
            .orElseThrow { throw RuntimeException("ErrorCode.ISSUE_NOT_FOUND") }
        issue.checkConfirmed(checkConfirmed)
        return KotlinMessageResponse(message = "성공적으로 처리되었습니다.", statusCode = 200)
    }
}