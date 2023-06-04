package kr.co.bookand.backend.notice.domain.dto;

import kr.co.bookand.backend.notice.domain.Notice;
import lombok.Builder;

public class NoticeDto {

    public record NoticeResponse(
            Long id,
            String title,
            String content,
            String createdAt
    ) {
        public static NoticeResponse of(Notice notice) {
            return new NoticeResponse(
                    notice.getId(),
                    notice.getTitle(),
                    notice.getContent(),
                    notice.getCreatedAt()
            );
        }
    }

    public record CreateNoticeRequest(
            String title,
            String content
    ) {
        @Builder
        public CreateNoticeRequest {
        }

        public Notice toEntity() {
            return Notice.builder()
                    .title(title)
                    .content(content)
                    .build();
        }
    }
}
