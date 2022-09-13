package kr.co.bookand.backend.bookmark;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.bookand.backend.account.Account;
import kr.co.bookand.backend.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private BookmarkType bookmarkType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @JsonIgnore
    @OneToMany(mappedBy = "bookStore", cascade = CascadeType.ALL)
    private List<BookMarkBookStore> mark_bookStoreList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<BookMarkArticle> mark_articleList = new ArrayList<>();

}
