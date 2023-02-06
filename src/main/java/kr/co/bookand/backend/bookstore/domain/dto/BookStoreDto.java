package kr.co.bookand.backend.bookstore.domain.dto;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.BookStoreImage;
import kr.co.bookand.backend.bookstore.domain.BookstoreStatus;
import kr.co.bookand.backend.bookstore.domain.BookstoreTheme;
import kr.co.bookand.backend.common.domain.PageResponse;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class BookStoreDto {

    public record BookStoreRequest(
            String name,
            String address,
            String businessHours,
            String contact,
            String facility,
            String sns,
            String theme,
            String mainImage,
            List<String> subImage,

            String introduction,
            String status,

            int view,
            int bookmark
    ) {
        @Builder
        public BookStoreRequest {
        }

        public BookStore toEntity(List<BookStoreImage> bookStoreImageList) {
            return BookStore.builder()
                    .name(name)
                    .address(address)
                    .businessHours(businessHours)
                    .contact(contact)
                    .facility(facility)
                    .sns(sns)
                    .theme(BookstoreTheme.valueOf(theme))
                    .mainImage(mainImage)
                    .subImages(bookStoreImageList)
                    .introduction(introduction)
                    .status(BookstoreStatus.INVISIBLE)
                    .view(view)
                    .bookmark(bookmark)
                    .build();
        }
    }

    public record BookStoreResponse(
            Long id,
            String name,
            BookStoreInfo info,
            BookstoreTheme theme,
            String introduction,
            String mainImage,
            List<String> subImage,
            String status,
            int view,
            int bookmark,
            String createdDate,
            String modifiedDate
    ) {
        @Builder
        public BookStoreResponse {
        }

        public static BookStoreResponse of(BookStore bookStore) {
            BookStoreInfo bookStoreInfo = BookStoreInfo.builder()
                    .address(bookStore.getAddress())
                    .businessHours(bookStore.getBusinessHours())
                    .contact(bookStore.getContact())
                    .facility(bookStore.getFacility())
                    .sns(bookStore.getSns())
                    .build();

            List<BookStoreImage> subImages = bookStore.getSubImages();
            List<String> subImageList = new ArrayList<>();
            for (BookStoreImage subImage : subImages) {
                subImageList.add(subImage.getUrl());
            }

            return BookStoreResponse.builder()
                    .id(bookStore.getId())
                    .name(bookStore.getName())
                    .info(bookStoreInfo)
                    .theme(bookStore.getTheme())
                    .mainImage(bookStore.getMainImage())
                    .subImage(subImageList)
                    .introduction(bookStore.getIntroduction())
                    .status(bookStore.getStatus().toString())
                    .view(bookStore.getView())
                    .bookmark(bookStore.getBookmark())
                    .createdDate(bookStore.getCreatedAt())
                    .modifiedDate(bookStore.getModifiedAt())
                    .status(bookStore.getStatus().toString())
                    .build();
        }

        public BookStore toEntity() {
            return BookStore.builder()
                    .id(id)
                    .name(name)
                    .address(info.address())
                    .businessHours(info.businessHours())
                    .contact(info.contact())
                    .facility(info.facility())
                    .sns(info.sns())
                    .theme(theme)
                    .mainImage(mainImage)
                    .introduction(introduction)
                    .status(BookstoreStatus.valueOf(status))
                    .view(view)
                    .bookmark(bookmark)
                    .build();
        }


    }

    public record BookStoreListRequest(
            List<Long> bookStoreDtoList
    ) {
    }

    public record BookStoreSimpleDto(
            Long bookStoreId
    ) {
    }

    public record BookStoreInfo(
            String address,
            String businessHours,
            String contact,
            String facility,
            String sns

    ) {
        @Builder
        public BookStoreInfo {
        }
    }

    public record BookStoreResponseList(
            Page<BookStoreResponse> bookStoreResponseList
    ) {
        @Builder
        public BookStoreResponseList {
        }
    }

    public record BookStorePageResponse(
            PageResponse<BookStoreResponse> bookstore
    ) {
        @Builder
        public BookStorePageResponse {
        }

        public static BookStorePageResponse of(Page<BookStoreResponse> bookStoreResponsePage) {
            return BookStorePageResponse.builder()
                    .bookstore(PageResponse.of(bookStoreResponsePage))
                    .build();
        }

    }
}
