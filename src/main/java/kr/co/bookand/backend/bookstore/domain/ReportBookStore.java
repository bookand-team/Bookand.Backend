package kr.co.bookand.backend.bookstore.domain;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto;
import kr.co.bookand.backend.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportBookStore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_bookstore_id")
    private Long id;

    private String name;
    private String address;

    private Boolean isAnswered;
    private String answerTitle;
    private String answerContent;
    private String answeredAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public void updateAnswer(BookStoreDto.AnswerReportRequest answerReportRequest) {
        this.isAnswered = true;
        this.answerTitle = answerReportRequest.answerTitle();
        this.answerContent = answerReportRequest.answerContent();
        this.answeredAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}
