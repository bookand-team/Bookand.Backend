package kr.co.bookand.backend.bookmark.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    private String folderName;

    private String folderImage;

    @Enumerated(EnumType.STRING)
    private BookmarkType bookmarkType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @JsonIgnore
    @OneToMany(mappedBy = "bookmark", cascade = CascadeType.ALL)
    private List<BookmarkBookStore> bookmarkBookStoreList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "bookmark", cascade = CascadeType.ALL)
    private List<BookmarkArticle> bookmarkArticleList = new ArrayList<>();

    public void updateFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void updateBookmarkBookStore(List<BookmarkBookStore> bookmarkBookStore) {
        this.bookmarkBookStoreList = bookmarkBookStore;
    }

    public void updateBookmarkArticle(List<BookmarkArticle> bookmarkArticle) {
        this.bookmarkArticleList = bookmarkArticle;
    }

}
