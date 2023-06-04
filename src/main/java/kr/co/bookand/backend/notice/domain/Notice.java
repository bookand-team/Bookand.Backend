package kr.co.bookand.backend.notice.domain;

import kr.co.bookand.backend.common.domain.BaseEntity;
import kr.co.bookand.backend.common.domain.DeviceOSFilter;
import kr.co.bookand.backend.common.domain.MemberIdFilter;
import kr.co.bookand.backend.common.domain.Status;
import kr.co.bookand.backend.notice.domain.dto.NoticeDto.UpdateNoticeRequest;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    private String title;
    private String content;
    private String image;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    @Enumerated(EnumType.STRING)
    private DeviceOSFilter deviceOSFilter;
    @Enumerated(EnumType.STRING)
    private MemberIdFilter memberIdFilter;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime displayAt;

    public void updateNoticeData(UpdateNoticeRequest updateNoticeRequest) {
        this.title = updateNoticeRequest.title();
        this.content = updateNoticeRequest.content();
        this.image = updateNoticeRequest.image();
        this.noticeType = NoticeType.valueOf(updateNoticeRequest.noticeType());
        this.deviceOSFilter = DeviceOSFilter.valueOf(updateNoticeRequest.targetType());
        this.memberIdFilter = MemberIdFilter.valueOf(updateNoticeRequest.targetNum());
    }

    public void updateNoticeStatus(Status status) {
        this.status = status;
    }

    public void updateDisplayAt(LocalDateTime displayAt) {
        this.displayAt = displayAt;
    }

}