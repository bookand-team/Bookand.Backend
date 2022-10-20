package kr.co.bookand.backend.article.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.bookand.backend.article.domain.dto.ArticleDto;
import kr.co.bookand.backend.bookmark.BookMarkArticle;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    private String mainPicture;

    private Integer hit;

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<BookMarkArticle> markArticleList = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<BookStore> bookStoreList = new ArrayList();

    public void viewCount() {
        this.hit = hit + 1;
    }

    public void updateArticle(ArticleDto.ArticleRequest articleDto) {
        this.title = articleDto.getTitle();
        this.content = articleDto.getContent();
        this.mainPicture = articleDto.getMainPicture();
    }
}
