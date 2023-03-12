package kr.co.bookand.backend.notification.repository;

import kr.co.bookand.backend.notification.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByVisibility(Pageable pageable, boolean visibility);
}
