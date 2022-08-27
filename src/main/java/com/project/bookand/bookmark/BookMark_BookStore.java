package com.project.bookand.bookmark;

import com.project.bookand.account.Account;
import com.project.bookand.bookstore.BookStore;
import com.project.bookand.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMark_BookStore extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private BookStore bookStore;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bookmark bookmark;
}
