package kr.co.bookand.backend.issue.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.common.domain.Message;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import kr.co.bookand.backend.issue.domain.Issue;
import kr.co.bookand.backend.issue.domain.IssueImage;
import kr.co.bookand.backend.issue.repository.IssueImageRepository;
import kr.co.bookand.backend.issue.repository.IssueRepository;
import kr.co.bookand.backend.util.s3.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static kr.co.bookand.backend.issue.domain.dto.IssueDto.*;
import static kr.co.bookand.backend.util.s3.dto.AwsS3Dto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class IssueService {

    private final IssueRepository issueRepository;
    private final IssueImageRepository issueImageRepository;
    private final AccountService accountService;
    private final AwsS3Service awsS3Service;


    @Transactional
    public IssueIdResponse createIssue (CreateIssueRequest createIssueRequest) {
        Account loginAccount = accountService.getCurrentAccount();
        accountService.isAccountAdmin();
        List<String> issueImages = createIssueRequest.issueImages();


        FileDto fileDto = null;
        if (createIssueRequest.logFile() != null) {
            MultipartFile multipartFile = createIssueRequest.logFile();
            fileDto = awsS3Service.uploadV2(multipartFile, "reportLog", loginAccount.getId().toString());
        }
        Issue issue = createIssueRequest.toEntity(loginAccount.getId());

        if (createIssueRequest.sendLogs()) {
            issue.setLogFile(fileDto.fileUrl(), fileDto.filename());
        }

        Issue saveIssue = issueRepository.save(issue);
        List<IssueImage> issueImageList = issueImages
                .stream()
                .map(issueImage -> {
                    IssueImage image = IssueImage.builder().imageUrl(issueImage).issue(saveIssue).build();
                    IssueImage saveIssueImage = issueImageRepository.save(image);
                    return saveIssueImage;
                })
                .toList();
        issue.setIssueImage(issueImageList);

        return IssueIdResponse.of(issue);
    }

    public IssueResponse getIssue(Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow();
        return IssueResponse.of(issue);
    }

    public PageResponse<IssueSimpleResponse> getIssueList(Pageable pageable) {
        accountService.isAccountAdmin();
        return PageResponse.of(issueRepository.findAll(pageable).map(IssueSimpleResponse::of));
    }

    @Transactional
    public Message checkConfirmed(Long issueId, boolean checkConfirmed) {
        Issue issue = issueRepository.findById(issueId).orElseThrow();
        issue.checkConfirmed(checkConfirmed);
        return Message.of("오류 및 버그 신고 체크 완료");
    }
}
