package kr.co.bookand.backend.article.domain.dto;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ArticleDto {

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class ArticleRequest {
        private String title;
        private String content;
        private String mainPicture;
        private String bookStoreList;

        public Article toArticle(List<BookStoreDto> bookStoreIdList) {
            List<BookStore> bookStores = new ArrayList<>();
            for (BookStoreDto b : bookStoreIdList) {
                bookStores.add(b.toBookStore());
            }
            return Article.builder()
                    .title(title)
                    .content(content)
                    .mainPicture(mainPicture)
                    .bookStoreList(bookStores)
                    .build();
        }

        public static ArticleResponse of(Article article) {
            List<BookStoreDto> bookStoreDtoList = new ArrayList<>();
            for (BookStore b : article.getBookStoreList()) {
                bookStoreDtoList.add(BookStoreDto.of(b));
            }
            return ArticleResponse.builder()
                    .title(article.getTitle())
                    .content(article.getTitle())
                    .mainPicture(article.getMainPicture())
                    .bookStoreDto(bookStoreDtoList)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ArticleResponse{
        private String title;
        private String content;
        private String mainPicture;
        private Integer hit;
        private List<BookStoreDto> bookStoreDto;

    }
}
