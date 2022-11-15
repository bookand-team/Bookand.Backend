package kr.co.bookand.backend.bookstore.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookStoreListDto {
    private List<Long> bookStoreDtoList;
}
