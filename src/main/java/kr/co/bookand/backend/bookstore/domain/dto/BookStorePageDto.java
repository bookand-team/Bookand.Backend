package kr.co.bookand.backend.bookstore.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookStorePageDto {
    private List<BookStoreDto> bookStoreDtoList;
    private int totalPages;
    private int currenPage;

    public static BookStorePageDto of(List<BookStoreDto> bookStoreDtoList, int totalPages, int currenPage) {
        return BookStorePageDto.builder()
                .bookStoreDtoList(bookStoreDtoList)
                .totalPages(totalPages)
                .currenPage(currenPage)
                .build();
    }
}
