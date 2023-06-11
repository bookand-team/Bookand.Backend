package kr.co.bookand.backend.notice.domain.dto;

import kr.co.bookand.backend.common.domain.DeviceOSFilter;
import kr.co.bookand.backend.common.domain.MemberIdFilter;
import kr.co.bookand.backend.common.domain.Status;
import kr.co.bookand.backend.notice.domain.Notice;
import kr.co.bookand.backend.notice.domain.NoticeType;
import lombok.Builder;

public class NoticeDto {

    public record NoticeResponse(
            Long id,
            String title,
            String content,
            String createdAt,
            String image,
            String targetType,
            String targetNum
    ) {
        public static NoticeResponse of(Notice notice) {
            return new NoticeResponse(
                    notice.getId(),
                    notice.getTitle(),
                    notice.getContent(),
                    notice.getCreatedAt(),
                    notice.getImage(),
                    notice.getDeviceOSFilter().toString(),
                    notice.getMemberIdFilter().toString()
            );
        }
    }

    public record NoticeWebResponse(
            Long id,
            String title,
            String content,
            String image,
            String createdAt,
            String modifiedAt,
            String status,
            String noticeType,
            String targetType,
            String targetNum
    ) {
        public static NoticeWebResponse of(Notice notice) {
            return new NoticeWebResponse(
                    notice.getId(),
                    notice.getTitle(),
                    notice.getContent(),
                    notice.getImage(),
                    notice.getCreatedAt(),
                    notice.getModifiedAt(),
                    notice.getStatus().toString(),
                    notice.getNoticeType().toString(),
                    notice.getDeviceOSFilter().toString(),
                    notice.getMemberIdFilter().toString()
            );
        }
    }

    public record CreateNoticeRequest(
            String title,
            String content,
            String image,
            String noticeType,
            String targetType,
            String targetNum
    ) {
        @Builder
        public CreateNoticeRequest {
        }

        public Notice toEntity() {
            return Notice.builder()
                    .title(title)
                    .content(content)
                    .image(image)
                    .noticeType(NoticeType.valueOf(noticeType))
                    .deviceOSFilter(DeviceOSFilter.valueOf(targetType))
                    .memberIdFilter(MemberIdFilter.valueOf(targetNum))
                    .status(Status.INVISIBLE)
                    .build();
        }
    }

    public record UpdateNoticeRequest(
            String title,
            String content,
            String image,
            String noticeType,
            String targetType,
            String targetNum
    ) {
        @Builder
        public UpdateNoticeRequest {
        }
    }

    public record NoticeMessage(String message) {
    }

    public record NoticeIdResponse(Long id) {
    }
}
