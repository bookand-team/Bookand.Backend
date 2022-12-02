package kr.co.bookand.backend.article.domain.dto;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto;
import kr.co.bookand.backend.common.Status;
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
        private Long id;
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
                    .id(id)
                    .title(title)
                    .content(content)
                    .mainPicture(mainPicture)
                    .bookStoreList(bookStores)
                    .status(Status.INVISIBLE)
                    .build();
        }

        public static ArticleResponse of(Article article) {
            List<BookStoreDto> bookStoreDtoList = new ArrayList<>();
            for (BookStore b : article.getBookStoreList()) {
                bookStoreDtoList.add(BookStoreDto.of(b));
            }
            return ArticleResponse.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .content(article.getTitle())
                    .mainPicture(article.getMainPicture())
                    .bookStoreDto(bookStoreDtoList)
                    .status(article.getStatus().name())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ArticleResponse{
        private Long id;
        private String title;
        private String content;
        private String mainPicture;
        private Integer hit;
        private List<BookStoreDto> bookStoreDto;
        private String status;

    }
}
