package kr.co.bookand.backend.notification;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.common.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

}