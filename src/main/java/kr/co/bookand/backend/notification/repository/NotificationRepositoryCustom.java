package kr.co.bookand.backend.notification.repository;

import kr.co.bookand.backend.notification.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationRepositoryCustom {
    Page<Notification> findAllByVisibility(Pageable pageable, boolean visibility, Long cursorId);
}
