package kr.co.bookand.backend.article.domain.dto;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.ArticleCategory;
import kr.co.bookand.backend.article.domain.ArticleTag;
import kr.co.bookand.backend.common.domain.DeviceOSFilter;
import kr.co.bookand.backend.common.domain.MemberIdFilter;
import kr.co.bookand.backend.common.domain.Status;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import lombok.Builder;
import org.springframework.data.domain.Page;

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


    // 아티클 상세 조회(APP)
    public record ArticleResponse(
            Long id,
            String title,
            String mainImage,
            String content,
            ArticleCategory category,
            String writer,
            Status status,
            int view,
            int bookmarkCount,
            boolean bookmark,
            boolean visibility,
            List<String> articleTagList,
            List<BookStoreSimpleResponse> bookStoreList,
            ArticleFilter filter,
            String createdDate,
            String modifiedDate,
            String displayDate
    ) {
        @Builder
        public ArticleResponse {
        }

        public static ArticleResponse of(Article article, List<BookStoreSimpleResponse> bookStoreList, boolean bookmark) {
            int bookmarkCount = article.getMarkArticleList() != null ? article.getMarkArticleList().size() : 0;
            return ArticleResponse.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .mainImage(article.getMainImage())
                    .content(article.getContent())
                    .category(article.getCategory())
                    .writer(article.getWriter())
                    .status(article.getStatus())
                    .view(article.getView())
                    .bookmarkCount(bookmarkCount)
                    .bookmark(bookmark)
                    .articleTagList(article.getArticleTagList().stream().map(ArticleTag::getTag).toList())
                    .bookStoreList(bookStoreList)
                    .visibility(article.isVisibility())
                    .filter(ArticleFilter.of(article))
                    .createdDate(article.getCreatedAt())
                    .modifiedDate(article.getModifiedAt())
                    .displayDate(article.getDisplayDate().toString())
                    .build();
        }
    }

    // 아티클 상세 조회 (WEB)
    public record ArticleDetailResponse(
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
            List<BookStoreSimpleResponse> bookStoreList,
            ArticleFilter filter,
            String createdDate,
            String modifiedDate,
            String displayDate
    ) {
        @Builder
        public ArticleDetailResponse {
        }

        public static ArticleDetailResponse of(Article article, List<BookStoreSimpleResponse> bookStoreList) {
            int bookmark = article.getMarkArticleList() != null ? article.getMarkArticleList().size() : 0;
            return ArticleDetailResponse.builder()
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
                    .bookStoreList(bookStoreList)
                    .visibility(article.isVisibility())
                    .filter(ArticleFilter.of(article))
                    .createdDate(article.getCreatedAt())
                    .modifiedDate(article.getModifiedAt())
                    .displayDate(article.getDisplayDate().toString())
                    .build();
        }
    }

    public record ArticleFilter(
            DeviceOSFilter deviceOS,
            MemberIdFilter memberId
    ) {
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

    // 아티클 리스트 조회 (WEB)
    public record ArticleWebResponse(
            Long id,
            String title,
            String mainImage,
            String content,
            ArticleCategory category,
            String writer,
            Status status,
            int view,
            List<String> articleTagList,
            boolean visibility,
            String createdDate,
            String modifiedDate,
            String displayDate
    ) {
        @Builder
        public ArticleWebResponse {
        }

        public static ArticleWebResponse of(Article article) {
            return ArticleWebResponse.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .mainImage(article.getMainImage())
                    .content(article.getContent())
                    .category(article.getCategory())
                    .writer(article.getWriter())
                    .status(article.getStatus())
                    .view(article.getView())
                    .articleTagList(article.getArticleTagList().stream().map(ArticleTag::getTag).toList())
                    .createdDate(article.getCreatedAt())
                    .modifiedDate(article.getModifiedAt())
                    .displayDate(article.getDisplayDate().toString())
                    .visibility(article.isVisibility())
                    .build();
        }
    }


    // 아티클 전체조회 (WEB)
    public record ArticleWebPageResponse(
            PageResponse<ArticleWebResponse> article
    ) {
        @Builder
        public ArticleWebPageResponse {
        }

        public static ArticleWebPageResponse of(Page<ArticleWebResponse> article) {
            return ArticleWebPageResponse.builder()
                    .article(PageResponse.of(article))
                    .build();
        }

    }

    // 아티클 서점 조회용 (APP)
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

    // 아티클 전체 조회 (APP)
    public record ArticleSimplePageResponse(
            PageResponse<ArticleSimpleResponse> data
    ) {
        @Builder
        public ArticleSimplePageResponse {
        }

        public static ArticleSimplePageResponse of(Page<ArticleSimpleResponse> article) {
            return ArticleSimplePageResponse.builder()
                    .data(PageResponse.of(article))
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

    public record ArticleIdResponse(
            Long id
    ) {
        @Builder
        public ArticleIdResponse {
        }

        public static ArticleIdResponse of(Article article) {
            return ArticleIdResponse.builder()
                    .id(article.getId())
                    .build();
        }
    }
}
