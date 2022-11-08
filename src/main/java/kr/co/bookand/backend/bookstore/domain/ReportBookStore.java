package kr.co.bookand.backend.bookstore.domain;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportBookStore extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;
}