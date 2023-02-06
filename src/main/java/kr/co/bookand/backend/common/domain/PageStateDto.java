package kr.co.bookand.backend.common.domain;

public class PageStateDto {

    public record PageStateRequest(
            String category,
            String theme,
            int page,
            String role,
            int row,
            String search,
            String status
    ) {

    }
}
