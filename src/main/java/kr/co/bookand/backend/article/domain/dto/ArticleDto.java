package kr.co.bookand.backend.article.domain.dto;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.ArticleBookStore;
import kr.co.bookand.backend.article.domain.ArticleCategory;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.common.domain.DeviceOSFilter;
import kr.co.bookand.backend.common.domain.MemberIdFilter;
import kr.co.bookand.backend.common.domain.Status;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto.*;

public class ArticleDto {

    public record ArticleRequest(
            String title,
            String content,
            String category,
            String writer,
            String status,
            List<Long> bookStoreList
    ) {
        public Article toEntity() {
            return Article.builder()
                    .title(title)
                    .content(content)
                    .category(ArticleCategory.valueOf(category))
                    .writer(writer)
                    .status(Status.INVISIBLE)
                    .articleBookStoreList(null)
                    .markArticleList(null)
                    .deviceOSFilter(DeviceOSFilter.ALL)
                    .memberIdFilter(MemberIdFilter.ALL)
                    .build();
        }
    }

    public record ArticleResponse(
            Long id,
            String title,
            String content,
            ArticleCategory category,
            String writer,
            Status status,
            int view,
            int bookmark,
            String createdDate,
            String modifiedDate,
            boolean visibility,
            List<BookStoreResponse> bookStoreList,
            ArticleFilter filter
    ) {
        @Builder
        public ArticleResponse {
        }

        public static ArticleResponse of(Article article) {
            int bookmark = article.getMarkArticleList() != null ? article.getMarkArticleList().size() : 0;
            List<ArticleBookStore> articleBookStoreList = article.getArticleBookStoreList();
            List<BookStore> bookStoreList = new ArrayList<>();
            for (ArticleBookStore articleBookStore : articleBookStoreList) {
                bookStoreList.add(articleBookStore.getBookStore());
            }
            return ArticleResponse.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .category(article.getCategory())
                    .writer(article.getWriter())
                    .status(article.getStatus())
                    .view(article.getView())
                    .bookmark(bookmark)
                    .createdDate(article.getCreatedAt())
                    .modifiedDate(article.getModifiedAt())
                    .bookStoreList(bookStoreList.stream().map(BookStoreResponse::of).toList())
                    .visibility(article.isVisibility())
                    .filter(ArticleFilter.of(article))
                    .build();
        }
    }

    public record ArticleFilter(
            DeviceOSFilter deviceOS,
            MemberIdFilter memberId
    ){
        @Builder
        public ArticleFilter {
        }

        public static ArticleFilter of(Article article) {
            return ArticleFilter.builder()
                    .deviceOS(article.getDeviceOSFilter())
                    .memberId(article.getMemberIdFilter())
                    .build();
        }

    }

    public record ArticleListRequest(
            List<Long> articleIdList
    ) {
    }

    public record ArticlePageResponse(
            PageResponse<ArticleResponse> article
    )
    {
        @Builder
        public ArticlePageResponse {
        }

        public static ArticlePageResponse of(Page<ArticleResponse> article) {
            return ArticlePageResponse.builder()
                    .article(PageResponse.of(article))
                    .build();
        }

    }
}
