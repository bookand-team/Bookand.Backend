package kr.co.bookand.backend.article.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.bookand.backend.article.domain.dto.ArticleDto;
import kr.co.bookand.backend.bookmark.BookMarkArticle;
import kr.co.bookand.backend.common.domain.BaseEntity;
import kr.co.bookand.backend.common.domain.DeviceOSFilter;
import kr.co.bookand.backend.common.domain.MemberIdFilter;
import kr.co.bookand.backend.common.domain.Status;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private ArticleCategory category;
    private String writer;
    private int view;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private DeviceOSFilter deviceOSFilter;

    @Enumerated(EnumType.STRING)
    private MemberIdFilter memberIdFilter;

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookMarkArticle> markArticleList = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<ArticleBookStore> articleBookStoreList = new ArrayList();

    public void viewCount() {
        this.view = view + 1;
    }

    public void updateArticle(ArticleDto.ArticleRequest articleRequest) {
        this.title = articleRequest.title();
        this.content = articleRequest.content();
        this.category = ArticleCategory.valueOf(articleRequest.category());
        this.status = Status.valueOf(articleRequest.status());
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
}
