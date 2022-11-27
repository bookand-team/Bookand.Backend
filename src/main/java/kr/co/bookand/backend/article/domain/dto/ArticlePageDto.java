package kr.co.bookand.backend.article.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ArticlePageDto {
    private List<ArticleDto.ArticleResponse> articleDtoList;
    private int totalPages;
    private int currenPage;

    public static ArticlePageDto of(List<ArticleDto.ArticleResponse> articleDtoList, int totalPages, int currenPage) {
        return ArticlePageDto.builder()
                .articleDtoList(articleDtoList)
                .totalPages(totalPages)
                .currenPage(currenPage)
                .build();
    }
}