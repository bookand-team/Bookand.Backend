package kr.co.bookand.backend.notification.service;

import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import kr.co.bookand.backend.common.exception.ErrorCode;
import kr.co.bookand.backend.notification.domain.Notification;
import kr.co.bookand.backend.notification.domain.dto.NotificationDto.*;
import kr.co.bookand.backend.notification.exception.NotificationException;
import kr.co.bookand.backend.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AccountService accountService;

    public PageResponse<NotificationResponse> getNotificationList(Pageable pageable, Long cursorId) {
        Page<NotificationResponse> notificationList = notificationRepository.findAllByVisibility(pageable, true, cursorId)
                .map(NotificationResponse::of);
        return PageResponse.of(notificationList);
    }

    @Transactional
    public NotificationResponse createNotification(CreateNotificationRequest createNotificationRequest) {
        accountService.isAccountAdmin();
        Notification notification = createNotificationRequest.toEntity();
        notificationRepository.save(notification);
        return NotificationResponse.of(notification);
    }

    public NotificationResponse getNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException(ErrorCode.NOT_FOUND_NOTIFICATION, notificationId));
        return NotificationResponse.of(notification);
    }
}
