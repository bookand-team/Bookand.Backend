package kr.co.bookand.backend.notification.repository;

import kr.co.bookand.backend.notification.domain.Notification;
import kr.co.bookand.backend.notification.domain.dto.NotificationDto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<NotificationListResponse> findAllByVisibility(Pageable pageable, boolean visibility);
}
