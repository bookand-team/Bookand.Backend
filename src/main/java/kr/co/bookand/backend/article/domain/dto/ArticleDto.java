package kr.co.bookand.backend.article.domain.dto;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.Category;
import kr.co.bookand.backend.common.DeviceType;
import kr.co.bookand.backend.common.UserType;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto;
import kr.co.bookand.backend.common.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ArticleDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ArticleRequest {
        private String title;
        private String content;
        private String category;
        private String mainPicture;
        private String bookStoreList;
        private String targetDevice;
        private String targetUser;

        public Article toArticle(List<BookStoreDto> bookStoreIdList) {
            List<BookStore> bookStores = new ArrayList<>();
            for (BookStoreDto b : bookStoreIdList) {
                bookStores.add(b.toBookStore());
            }
            return Article.builder()
                    .title(title)
                    .content(content)
                    .category(Category.valueOf(category))
                    .mainPicture(mainPicture)
                    .bookStoreList(bookStores)
                    .status(Status.INVISIBLE)
                    .deviceType(DeviceType.valueOf(targetDevice))
                    .userType(UserType.valueOf(targetUser))
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
                    .content(article.getContent())
                    .category(article.getCategory().toString())
                    .mainPicture(article.getMainPicture())
                    .bookStoreDto(bookStoreDtoList)
                    .status(article.getStatus().toString())
                    .targetDevice(article.getDeviceType().toString())
                    .targetUser(article.getUserType().toString())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ArticleResponse {
        private Long id;
        private String title;
        private String content;
        private String category;
        private String mainPicture;
        private String targetDevice;
        private String targetUser;
        private Integer hit;
        private List<BookStoreDto> bookStoreDto;
        private String status;
    }
}
