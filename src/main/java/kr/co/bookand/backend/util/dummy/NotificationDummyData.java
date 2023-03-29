package kr.co.bookand.backend.util.dummy;

import kr.co.bookand.backend.notification.domain.Notification;
import kr.co.bookand.backend.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationDummyData {

    private final NotificationRepository notificationRepository;

    @PostConstruct
    public void dummyData() {

        if (notificationRepository.count() > 0) {
            log.info("[00] 공지가 이미 존재하여 더미를 생성하지 않았습니다.");
            return;
        }

        for (int i = 0; i < 15; i++) {
            Notification notification = Notification.builder()
                    .title("공지사항(%d) 제목".formatted(i))
                    .content("공지사항(%d) 내용".formatted(i))
                    .build();
            notificationRepository.save(notification);
        }
    }
}
