package kr.co.bookand.backend.article.domain.dto;

import kr.co.bookand.backend.bookstore.domain.BookstoreTheme;
import kr.co.bookand.backend.common.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleSearchDto {
    private int page;
    private int row;
    private BookstoreTheme category;
    private Status status;
    private String search;
}