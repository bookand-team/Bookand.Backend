package kr.co.bookand.backend.common.domain.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {
    private final int totalPages;
    private final long totalElements;
    private final boolean last;
    private final List<T> content;

    public static <T> PageResponse<T> of(Page<T> page) {
        return PageResponse.<T>builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .last(page.isLast())
                .content(page.getContent())
                .build();
    }

    public static <T> PageResponse<T> ofCursor(Page<T> page, Long totalElements) {
        return PageResponse.<T>builder()
                .totalPages(page.getTotalPages())
                .totalElements(totalElements)
                .last(page.isLast())
                .content(page.getContent())
                .build();
    }

    public static long getTotalElements(long totalElements, Long cursorElements) {
        if (cursorElements != null) {
            totalElements += cursorElements;
        }
        return Math.max(totalElements, 0);
    }
}