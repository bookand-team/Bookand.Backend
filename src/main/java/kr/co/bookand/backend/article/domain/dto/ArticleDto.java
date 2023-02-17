package kr.co.bookand.backend.article.domain.dto;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.ArticleBookStore;
import kr.co.bookand.backend.article.domain.ArticleCategory;
import kr.co.bookand.backend.article.domain.ArticleTag;
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
            String mainImage,
            String content,
            String category,
            String writer,
            String status,
            List<String> tags,
            List<Long> bookStoreList
    ) {
        public Article toEntity() {
            return Article.builder()
                    .title(title)
                    .mainImage(mainImage)
                    .content(content)
                    .category(ArticleCategory.valueOf(category))
                    .writer(writer)
                    .status(Status.INVISIBLE)
                    .articleTagList(null)
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
            String mainImage,
            String content,
            ArticleCategory category,
            String writer,
            Status status,
            int view,
            int bookmark,
            boolean visibility,
            List<String> articleTagList,
            List<BookStoreResponse> bookStoreList,
            ArticleFilter filter,
            String createdDate,
            String modifiedDate
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
                    .mainImage(article.getMainImage())
                    .content(article.getContent())
                    .category(article.getCategory())
                    .writer(article.getWriter())
                    .status(article.getStatus())
                    .view(article.getView())
                    .bookmark(bookmark)
                    .articleTagList(article.getArticleTagList().stream().map(ArticleTag::getTag).toList())
                    .bookStoreList(bookStoreList.stream().map(BookStoreResponse::of).toList())
                    .visibility(article.isVisibility())
                    .filter(ArticleFilter.of(article))
                    .createdDate(article.getCreatedAt())
                    .modifiedDate(article.getModifiedAt())
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

    public record ArticleSimpleResponse(
            Long id,
            String title,
            String mainImage,
            String content,
            ArticleCategory category,
            String writer,
            Status status,
            int view,
            boolean isBookmark,
            List<String> articleTagList,
            boolean visibility,
            String createdDate,
            String modifiedDate
    ) {
        @Builder
        public ArticleSimpleResponse {
        }

        public static ArticleSimpleResponse of(Article article, boolean isBookmark) {
            return ArticleSimpleResponse.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .mainImage(article.getMainImage())
                    .content(article.getContent())
                    .category(article.getCategory())
                    .writer(article.getWriter())
                    .status(article.getStatus())
                    .view(article.getView())
                    .isBookmark(isBookmark)
                    .articleTagList(article.getArticleTagList().stream().map(ArticleTag::getTag).toList())
                    .createdDate(article.getCreatedAt())
                    .modifiedDate(article.getModifiedAt())
                    .visibility(article.isVisibility())
                    .build();
        }
    }

    public record ArticleSimplePageResponse(
            PageResponse<ArticleSimpleResponse> article
    )
    {
        @Builder
        public ArticleSimplePageResponse {
        }

        public static ArticleSimplePageResponse of(Page<ArticleSimpleResponse> article) {
            return ArticleSimplePageResponse.builder()
                    .article(PageResponse.of(article))
                    .build();
        }
    }

    // Tag 관련 DTO
    public record ArticleTagResponse(
            String tag
    ) {
        @Builder
        public ArticleTagResponse {
        }

        public static ArticleTagResponse of(ArticleTag articleTag) {
            return ArticleTagResponse.builder()
                    .tag(articleTag.getTag())
                    .build();
        }
    }
}
