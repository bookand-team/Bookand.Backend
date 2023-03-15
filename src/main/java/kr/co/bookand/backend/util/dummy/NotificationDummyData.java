package kr.co.bookand.backend.util.dummy;

import kr.co.bookand.backend.notification.domain.Notification;
import kr.co.bookand.backend.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class NotificationDummyData {

    private final NotificationRepository notificationRepository;

    @PostConstruct
    public void dummyData() {
        for (int i = 0; i < 15; i++) {
            Notification notification = Notification.builder()
                    .title("공지사항(%d) 제목".formatted(i))
                    .content("공지사항(%d) 내용".formatted(i))
                    .build();
            notificationRepository.save(notification);
        }
    }
}
