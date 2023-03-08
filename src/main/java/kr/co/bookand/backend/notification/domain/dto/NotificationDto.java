package kr.co.bookand.backend.notification.domain.dto;

import kr.co.bookand.backend.notification.domain.Notification;
import lombok.Builder;

public class NotificationDto {

    public record NotificationListResponse(
            Long id,
            String title
    ) {
        public static NotificationListResponse of(Notification notification) {
            return new NotificationListResponse(
                    notification.getId(),
                    notification.getTitle()
            );
        }
    }

    public record NotificationResponse(
            Long id,
            String title,
            String content,
            String createdAt
    ) {
        public static NotificationResponse of(Notification notification) {
            return new NotificationResponse(
                    notification.getId(),
                    notification.getTitle(),
                    notification.getContent(),
                    notification.getCreatedAt()
            );
        }
    }

    public record CreateNotificationRequest(
            String title,
            String content
    ) {
        @Builder
        public CreateNotificationRequest {
        }

        public Notification toEntity() {
            return Notification.builder()
                    .title(title)
                    .content(content)
                    .build();
        }
    }
}
