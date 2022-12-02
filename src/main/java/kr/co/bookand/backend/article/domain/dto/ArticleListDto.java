package kr.co.bookand.backend.article.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleListDto {
    private List<Long> articleDtoList;
}
