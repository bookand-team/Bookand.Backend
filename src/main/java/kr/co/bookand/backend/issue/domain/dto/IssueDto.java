package kr.co.bookand.backend.issue.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.common.domain.DeviceOSFilter;
import kr.co.bookand.backend.issue.domain.Issue;
import kr.co.bookand.backend.issue.domain.IssueImage;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class IssueDto {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    public record CreateIssueRequest(

            @ApiModelProperty(value = "오류 및 버그 신고 날짜 yyyy-MM-dd'T'HH:mm:ss")
            String issuedAt,
            @ApiModelProperty(value = "오류 및 버그 신고 내용")
            String issueContent,
            @ApiModelProperty(value = "오류 및 버그 신고 답변 이메일")
            String issueReportResponseEmail,
            @ApiModelProperty(value = "오류 및 버그 신고 이미지")
            List<String> issueImages,
            @ApiModelProperty(value = "로그 전송 여부")
            boolean sendLogs,
            @ApiModelProperty(value = "로그 파일")
            MultipartFile logFile,
            @ApiModelProperty(value = "유저 운영체제 IOS, ANDROID")
            DeviceOSFilter deviceOS
    ) {
        public Issue toEntity(Long accountId) {
            LocalDateTime parsedIssueAt = LocalDateTime.parse(issuedAt(), formatter);
            return Issue.builder()
                    .accountId(accountId)
                    .issuedAt(parsedIssueAt)
                    .issueContent(issueContent)
                    .issueReportResponseEmail(issueReportResponseEmail)
                    .sendLogs(sendLogs)
                    .deviceOS(deviceOS)
                    .checkConfirmed(false)
                    .build();
        }
    }

    public record IssueIdResponse(
            Long id
    ) {
        public static IssueIdResponse of(Issue issue) {
            return new IssueIdResponse(issue.getId());
        }
    }

    public record IssueSimpleResponse(
            @ApiModelProperty(value = "이슈 id 값")
            Long issueId,
            @ApiModelProperty(value = "오류 및 버그 신고 답변 이메일")
            String issueReportResponseEmail,
            @ApiModelProperty(value = "신고한 유저의 id 값")
            Long accountId,
            @ApiModelProperty(value = "오류 및 버그 신고 내용")
            String issueContent,
            @ApiModelProperty(value = "오류 및 버그 신고 발생 날짜 yyyy-MM-dd'T'HH:mm:ss")
            LocalDateTime issuedAt,
            @ApiOperation(value = "등록 일자")
            String createdAt,
            @ApiModelProperty(value = "확인 여부")
            boolean checkConfirmed

    ) {
        public static IssueSimpleResponse of(Issue issue) {
            return new IssueSimpleResponse(
                    issue.getId(),
                    issue.getIssueReportResponseEmail(),
                    issue.getAccountId(),
                    issue.getIssueContent(),
                    issue.getIssuedAt(),
                    issue.getCreatedAt(),
                    issue.isCheckConfirmed()
            );
        }
    }

    public record IssueResponse(
            @ApiModelProperty(value = "신고한 유저의 id 값")
            Long accountId,
            @ApiModelProperty(value = "오류 및 버그 신고 답변 이메일")
            String issueReportResponseEmail,
            @ApiModelProperty(value = "유저 운영체제 IOS, ANDROID")
            DeviceOSFilter deviceOS,
            @ApiOperation(value = "등록 일자")
            String createdAt,
            @ApiModelProperty(value = "로그 파일 경로")
            String logFilePath,


            @ApiModelProperty(value = "오류 및 버그 신고 발생 날짜 yyyy-MM-dd'T'HH:mm:ss")
            LocalDateTime issuedAt,
            @ApiModelProperty(value = "오류 및 버그 신고 내용")
            String issueContent,
            @ApiModelProperty(value = "오류 및 버그 신고 이미지")
            List<String> issueImages
    ) {

        public static IssueResponse of(Issue issue) {
            List<String> issueImages = issue.getIssueImages()
                    .stream()
                    .map(IssueImage::getImageUrl)
                    .toList();
            return new IssueResponse(
                    issue.getAccountId(),
                    issue.getIssueReportResponseEmail(),
                    issue.getDeviceOS(),
                    issue.getCreatedAt(),
                    issue.getLogFilePath(),
                    issue.getIssuedAt(),
                    issue.getIssueContent(),
                    issueImages
            );
        }
    }
}
