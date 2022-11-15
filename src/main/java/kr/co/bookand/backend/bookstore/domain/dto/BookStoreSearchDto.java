package kr.co.bookand.backend.bookstore.domain.dto;

import kr.co.bookand.backend.bookstore.domain.Theme;
import kr.co.bookand.backend.common.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BookStoreSearchDto {
    private int page;
    private int raw;
    private Theme category;
    private Status status;
    private String search;
}
