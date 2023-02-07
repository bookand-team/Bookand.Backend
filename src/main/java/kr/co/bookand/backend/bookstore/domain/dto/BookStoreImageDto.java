package kr.co.bookand.backend.bookstore.domain.dto;

import kr.co.bookand.backend.bookstore.domain.BookStoreImage;
import lombok.Builder;

public class BookStoreImageDto {

    public record BookStoreImageResponse(
            Long id,
            String url,
            String bookStore
    ) {
        @Builder
        public BookStoreImageResponse {
        }

        public static BookStoreImageResponse of(BookStoreImage bookStoreImage) {
            return BookStoreImageResponse.builder()
                    .id(bookStoreImage.getId())
                    .url(bookStoreImage.getUrl())
                    .bookStore(bookStoreImage.getBookStore().getName())
                    .build();
        }
    }
}
