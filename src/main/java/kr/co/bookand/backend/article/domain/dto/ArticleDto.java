package kr.co.bookand.backend.article.domain.dto;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ArticleDto {
    private String title;
    private String content;
    private String mainPicture;
    private Integer hit;
    private List<BookStoreDto> bookStoreDto;

    public static ArticleDto of(Article article) {
        return ArticleDto.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .mainPicture(article.getMainPicture())
                .hit(article.getHit())
                .build();
    }

    public Article toArticle() {
        return Article.builder()
                .title(title)
                .content(content)
                .mainPicture(mainPicture)
                .hit(hit)
                .build();
    }
}
