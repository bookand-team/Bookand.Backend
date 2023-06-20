package kr.co.bookand.backend.issue.service

import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.common.domain.MessageResponse
import kr.co.bookand.backend.common.exception.BookandException
import kr.co.bookand.backend.issue.domain.Issue
import kr.co.bookand.backend.issue.domain.IssueImage
import kr.co.bookand.backend.issue.domain.dto.*
import kr.co.bookand.backend.issue.repository.IssueImageRepository
import kr.co.bookand.backend.issue.repository.IssueRepository
import kr.co.bookand.backend.util.s3.dto.FileDto
import kr.co.bookand.backend.util.s3.service.AwsS3Service
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
class IssueService(
    private val issueRepository: IssueRepository,
    private val issueImageRepository: IssueImageRepository,
    private val awsS3Service: AwsS3Service
) {

    fun getIssue(issueId: Long): IssueResponse {
        return issueRepository.findById(issueId)
            .map { IssueResponse(it) }
            .orElseThrow { throw BookandException(ErrorCode.NOT_FOUND_ISSUE) }
    }

    fun getIssueList(currentAccount: Account, pageable: Pageable): IssueSimpleListResponse {
        currentAccount.role.checkAdminAndManager()
        val map = issueRepository.findAll(pageable)
            .map { IssueSimpleResponse(it) }
        return IssueSimpleListResponse(PageResponse.of(map))
    }

    @Transactional
    fun createIssue(
        currentAccount: Account,
        createIssueRequest: CreateIssueRequest
    ): IssueIdResponse {
        val issueImagesList = createIssueRequest.issueImages

        var fileDto = FileDto("", "")
        if (createIssueRequest.logFile != null) {
            val multipartFile = createIssueRequest.logFile
            fileDto = awsS3Service.uploadV2(multipartFile, "reportLog", "loginAccount")
        }
        val issue = Issue(createIssueRequest, 1L)

        if (createIssueRequest.sendLogs) {
            issue.setLogFile(fileDto.url, fileDto.fileName)
        }

        val saveIssue: Issue = issueRepository.save(issue)
        val issueImageList = issueImagesList
            .map { issueImage: String ->
                val image = IssueImage(issueImage, saveIssue)
                val saveIssueImage: IssueImage = issueImageRepository.save(image)
                saveIssueImage
            }
            .toList()
        issue.setIssueImage(issueImageList)

        return IssueIdResponse(saveIssue.id)
    }

    @Transactional
    fun checkConfirmed(issueId: Long, checkConfirmed: Boolean): MessageResponse {
        val issue: Issue = issueRepository.findById(issueId)
            .orElseThrow { throw BookandException(ErrorCode.NOT_FOUND_ISSUE) }
        issue.checkConfirmed(checkConfirmed)
        return MessageResponse(message = "성공적으로 처리되었습니다.", statusCode = 200)
    }
}