package kr.co.bookand.backend.article.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.bookand.backend.article.domain.dto.ArticleDto;
import kr.co.bookand.backend.bookmark.BookMarkArticle;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.common.domain.BaseTimeEntity;
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
public class Article extends BaseTimeEntity {

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

    @OneToMany(mappedBy = "article" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookStore> bookStoreList = new ArrayList();

    public void viewCount() {
        this.view = view + 1;
    }
    
}
