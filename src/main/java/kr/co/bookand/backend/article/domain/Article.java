package kr.co.bookand.backend.article.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.bookand.backend.bookmark.domain.BookmarkArticle;
import kr.co.bookand.backend.common.domain.BaseEntity;
import kr.co.bookand.backend.common.domain.DeviceOSFilter;
import kr.co.bookand.backend.common.domain.MemberIdFilter;
import kr.co.bookand.backend.common.domain.Status;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static kr.co.bookand.backend.article.domain.dto.ArticleDto.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    private String title;

    private String subTitle;
    private String content;
    private String mainImage;

    @Enumerated(EnumType.STRING)
    private ArticleCategory category;
    private String writer;
    private int view;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime displayAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private DeviceOSFilter deviceOSFilter;

    @Enumerated(EnumType.STRING)
    private MemberIdFilter memberIdFilter;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleTag> articleTagList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookmarkArticle> markArticleList = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<ArticleBookStore> articleBookStoreList = new ArrayList();

    public void viewCount() {
        this.view = view + 1;
    }

    public void updateArticle(ArticleRequest articleRequest) {
        this.title = articleRequest.title();
        this.content = articleRequest.content();
        this.category = ArticleCategory.valueOf(articleRequest.category());
        this.status = Status.INVISIBLE;
    }

    public void updateArticleStatus(Status status) {
        this.status = status;
    }

    public void addArticleBookStore(ArticleBookStore articleBookStore) {
        if (articleBookStoreList == null) {
            articleBookStoreList = new ArrayList<>();
        }
        this.articleBookStoreList.add(articleBookStore);
    }

    public void updateArticleBookStore(ArticleBookStore articleBookStore) {
        this.articleBookStoreList.add(articleBookStore);
    }
    public void removeArticleAndBookStore(ArticleBookStore articleBookStore) {
        this.articleBookStoreList.remove(articleBookStore);
    }

    public void removeBookmarkArticle(BookmarkArticle bookmarkArticle) {
        this.markArticleList.remove(bookmarkArticle);
    }

    public void updateArticleTagList(List<ArticleTag> articleTagList) {
        if (articleTagList == null) {
            articleTagList = new ArrayList<>();
        }
        this.articleTagList = articleTagList;
    }

    public void updateDisplayAt(LocalDateTime displayAt) {
        this.displayAt = displayAt;
    }
}
