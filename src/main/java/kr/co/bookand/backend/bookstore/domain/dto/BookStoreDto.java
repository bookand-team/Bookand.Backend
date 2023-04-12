package kr.co.bookand.backend.bookstore.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.bookstore.domain.*;
import kr.co.bookand.backend.common.domain.Status;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static kr.co.bookand.backend.article.domain.dto.ArticleDto.*;
import static kr.co.bookand.backend.bookstore.domain.dto.BookStoreImageDto.*;

public class BookStoreDto {

    // 서점 생성 request
    public record BookStoreRequest(
            String name,
            String address,
            String businessHours,
            String contact,
            String facility,
            String latitude,
            String longitude,
            String sns,
            String mainImage,
            @ApiModelProperty(
                    value = "서점 테마 리스트 (TRAVEL, MUSIC, PICTURE, PET, MOVIE, DETECTIVE, HISTORY, NONE)",
                    example = "TRAVEL/MUSIC/PICTURE/PET/MOVIE/DETECTIVE/HISTORY/NONE"
            )
            List<String> themeList,
            List<String> subImage,
            String introduction,
            String status,
            int view
    ) {
        @Builder
        public BookStoreRequest {
        }

        public BookStore toEntity(List<BookStoreImage> bookStoreImageList, List<BookStoreTheme> bookStoreThemeList, BookStoreVersion bookStoreVersion) {
            return BookStore.builder()
                    .name(name)
                    .address(address)
                    .businessHours(businessHours)
                    .contact(contact)
                    .facility(facility)
                    .latitude(latitude)
                    .longitude(longitude)
                    .sns(sns)
                    .mainImage(mainImage)
                    .themeList(bookStoreThemeList)
                    .subImages(bookStoreImageList)
                    .introduction(introduction)
                    .status(Status.INVISIBLE)
                    .displayDate(null)
                    .view(view)
                    .bookStoreVersion(bookStoreVersion)
                    .build();
        }
    }

    // 서점 일부 정보 response (아티클 내부, 앱 전체 조회)
    public record BookStoreSimpleResponse(
            Long id,
            String name,
            String introduction,
            String mainImage,
            List<String> themeList,
            boolean isBookmark
    ) {
        public static BookStoreSimpleResponse of(BookStore bookStore, boolean isBookmark) {
            return new BookStoreSimpleResponse(
                    bookStore.getId(),
                    bookStore.getName(),
                    bookStore.getIntroduction(),
                    bookStore.getMainImage(),
                    bookStore.getThemeList().stream()
                            .map(bookStoreTheme -> bookStoreTheme.getTheme().name())
                            .toList(),
                    isBookmark
            );
        }
    }

    // 서점 상세 조회 (APP)
    public record BookStoreResponse(
            Long id,
            String name,
            BookStoreInfo info,
            String introduction,
            String mainImage,
            List<BookStoreType> themeList,
            List<BookStoreImageResponse> subImage,
            String status,
            int view,
            boolean isBookmark,
            String createdDate,
            String modifiedDate,
            LocalDateTime displayDate,
            List<ArticleSimpleResponse> articleResponse,
            boolean visibility
    ) {
        @Builder
        public BookStoreResponse {
        }

        public static BookStoreResponse of(BookStore bookStore, boolean isBookmarkBookStore, List<ArticleSimpleResponse> articleList) {
            BookStoreInfo bookStoreInfo = BookStoreInfo.builder()
                    .address(bookStore.getAddress())
                    .businessHours(bookStore.getBusinessHours())
                    .contact(bookStore.getContact())
                    .facility(bookStore.getFacility())
                    .latitude(bookStore.getLatitude())
                    .longitude(bookStore.getLongitude())
                    .sns(bookStore.getSns())
                    .build();

            List<BookStoreImageResponse> subImages = bookStore.getSubImages().stream()
                    .map(BookStoreImageResponse::of)
                    .toList();

            return BookStoreResponse.builder()
                    .id(bookStore.getId())
                    .name(bookStore.getName())
                    .info(bookStoreInfo)
                    .themeList(bookStore.getThemeList().stream().map(BookStoreTheme::getTheme).toList())
                    .mainImage(bookStore.getMainImage())
                    .subImage(subImages)
                    .introduction(bookStore.getIntroduction())
                    .status(bookStore.getStatus().toString())
                    .view(bookStore.getView())
                    .isBookmark(isBookmarkBookStore)
                    .articleResponse(articleList)
                    .createdDate(bookStore.getCreatedAt())
                    .modifiedDate(bookStore.getModifiedAt())
                    .displayDate(bookStore.getDisplayDate())
                    .visibility(bookStore.isVisibility())
                    .status(bookStore.getStatus().toString())
                    .build();
        }
    }

    // 서점 상세 조회 (WEB)
    public record BookStoreWebResponse(
            Long id,
            String name,
            BookStoreInfo info,
            List<BookStoreType> theme,
            String introduction,
            String mainImage,
            String status,
            int view,
            String createdDate,
            String modifiedDate,
            LocalDateTime displayDate,
            boolean visibility
    ) {
        @Builder
        public BookStoreWebResponse {
        }

        public static BookStoreWebResponse of(BookStore bookStore) {
            BookStoreInfo bookStoreInfo = BookStoreInfo.builder()
                    .address(bookStore.getAddress())
                    .businessHours(bookStore.getBusinessHours())
                    .contact(bookStore.getContact())
                    .facility(bookStore.getFacility())
                    .latitude(bookStore.getLatitude())
                    .longitude(bookStore.getLongitude())
                    .sns(bookStore.getSns())
                    .build();

            return BookStoreWebResponse.builder()
                    .id(bookStore.getId())
                    .name(bookStore.getName())
                    .info(bookStoreInfo)
                    .theme(bookStore.getThemeList().stream().map(BookStoreTheme::getTheme).toList())
                    .mainImage(bookStore.getMainImage())
                    .introduction(bookStore.getIntroduction())
                    .status(bookStore.getStatus().toString())
                    .view(bookStore.getView())
                    .createdDate(bookStore.getCreatedAt())
                    .modifiedDate(bookStore.getModifiedAt())
                    .displayDate(bookStore.getDisplayDate())
                    .visibility(bookStore.isVisibility())
                    .status(bookStore.getStatus().toString())
                    .build();
        }
    }

    public record BookStoreVersionResponse(
            Long id,
            String name,
            String mainImage,
            List<BookStoreType> theme,
            String latitude,
            String longitude,
            Boolean isBookmark
            ) {
        public static BookStoreVersionResponse of(BookStore bookStore, boolean isBookmark) {
            return new BookStoreVersionResponse(
                    bookStore.getId(),
                    bookStore.getName(),
                    bookStore.getMainImage(),
                    bookStore.getThemeList().stream().map(BookStoreTheme::getTheme).toList(),
                    bookStore.getLatitude(),
                    bookStore.getLongitude(),
                    isBookmark
            );
        }
    }

    public record BookStoreVersionListResponse(
            List<BookStoreVersionResponse> bookStoreVersionListResponse,
            Long currentVersionId
    ) {
        public static BookStoreVersionListResponse of(List<BookStoreVersionResponse> bookStoreVersionListResponse, Long currentVersionId) {
            return new BookStoreVersionListResponse(bookStoreVersionListResponse, currentVersionId);
        }
    }

    // 서점 id 리스트 Request
    public record BookStoreListRequest(
            List<Long> bookStoreDtoList
    ) {
    }

    // 서점 Info
    public record BookStoreInfo(
            String address,
            String businessHours,
            String contact,
            String facility,
            String latitude,
            String longitude,
            String sns

    ) {
        @Builder
        public BookStoreInfo {
        }
    }

    // 서점 전체 리스트 조회(WEB 용)
    public record BookStorePageResponse(
            PageResponse<BookStoreWebResponse> bookstore
    ) {
        @Builder
        public BookStorePageResponse {
        }

        public static BookStorePageResponse of(Page<BookStoreWebResponse> bookStoreResponsePage) {
            return BookStorePageResponse.builder()
                    .bookstore(PageResponse.of(bookStoreResponsePage))
                    .build();
        }
    }


    // 서점 전체 리스트 조회(APP 용)
    public record BookStorePageResponseApp(
            PageResponse<BookStoreSimpleResponse> data
    ) {
        @Builder
        public BookStorePageResponseApp {
        }

        public static BookStorePageResponseApp of(Page<BookStoreSimpleResponse> bookStoreResponsePage) {
            return BookStorePageResponseApp.builder()
                    .data(PageResponse.of(bookStoreResponsePage))
                    .build();
        }
    }

    public record ReportBookStoreRequest(
            String name,
            String address

    ) {
        @Builder
        public ReportBookStoreRequest {
        }

        public ReportBookStore toEntity(Account account) {
            return ReportBookStore.builder()
                    .name(name)
                    .address(address)
                    .isAnswered(false)
                    .answerContent("미답변")
                    .account(account)
                    .build();
        }
    }

    public record AnswerReportRequest(
            String answerTitle,
            String answerContent
    ) {
        @Builder
        public AnswerReportRequest {
        }
    }

    public record BookStoreReportListResponse(
            Long reportId,
            String providerEmail,
            String bookStoreName,
            int reportCount,
            Boolean isAnswered,
            String createdAt,
            String answeredAt
    ) {
        @Builder
        public BookStoreReportListResponse {
        }

        public static BookStoreReportListResponse of(ReportBookStore reportBookStore) {
            return BookStoreReportListResponse.builder()
                    .reportId(reportBookStore.getId())
                    .providerEmail(reportBookStore.getAccount().getEmail())
                    .bookStoreName(reportBookStore.getName())
                    .reportCount(1)
                    .isAnswered(reportBookStore.getIsAnswered())
                    .createdAt(reportBookStore.getCreatedAt())
                    .answeredAt(reportBookStore.getAnsweredAt())
                    .build();
        }
    }
}
