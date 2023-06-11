package kr.co.bookand.backend.issue.domain;

import kr.co.bookand.backend.common.domain.BaseEntity;
import kr.co.bookand.backend.common.domain.DeviceOSFilter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Issue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long id;

    private LocalDateTime issuedAt;
    private String issueContent;
    private String issueReportResponseEmail;
    private boolean sendLogs;
    private String logFilePath;
    private String logFileName;
    private Long accountId;
    private boolean checkConfirmed;
    @Enumerated(EnumType.STRING)
    private DeviceOSFilter deviceOS;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    private List<IssueImage> issueImages = new ArrayList<>();

    public void setIssueImage(List<IssueImage> issueImage) {
        if (issueImages == null)
            issueImages = new ArrayList<>();
        issueImages.addAll(issueImage);
    }

    public void setLogFile(String logFilePath, String logFileName) {
        this.logFilePath = logFilePath;
        this.logFileName = logFileName;
    }

    public void checkConfirmed(boolean checkConfirmed) {
        this.checkConfirmed = checkConfirmed;
    }
}
