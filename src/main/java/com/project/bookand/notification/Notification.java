package com.project.bookand.notification;

import com.project.bookand.account.Account;
import com.project.bookand.common.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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