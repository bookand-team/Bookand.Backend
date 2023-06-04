package kr.co.bookand.backend.util.dummy;

import kr.co.bookand.backend.common.domain.DeviceOSFilter;
import kr.co.bookand.backend.common.domain.MemberIdFilter;
import kr.co.bookand.backend.common.domain.Status;
import kr.co.bookand.backend.notice.domain.Notice;
import kr.co.bookand.backend.notice.domain.NoticeType;
import kr.co.bookand.backend.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoticeDummyData {

    private final NoticeRepository noticeRepository;

    @PostConstruct
    public void dummyData() {

        if (noticeRepository.count() > 0) {
            log.info("[00] 공지가 이미 존재하여 더미를 생성하지 않았습니다.");
            return;
        }

        for (int i = 0; i < 5; i++) {
            Notice notice = Notice.builder()
                    .title("공지사항(%d) 제목".formatted(i))
                    .content("공지사항(%d) 내용".formatted(i))
                    .image("img")
                    .status(Status.VISIBLE)
                    .noticeType(NoticeType.ETC)
                    .deviceOSFilter(DeviceOSFilter.IOS)
                    .memberIdFilter(MemberIdFilter.ALL)
                    .build();
            noticeRepository.save(notice);
        }
        for (int i = 5; i < 10; i++) {
            Notice notice = Notice.builder()
                    .title("공지사항(%d) 제목".formatted(i))
                    .content("공지사항(%d) 내용".formatted(i))
                    .image("img")
                    .status(Status.VISIBLE)
                    .noticeType(NoticeType.ETC)
                    .deviceOSFilter(DeviceOSFilter.ANDROID)
                    .memberIdFilter(MemberIdFilter.EVEN)
                    .build();
            noticeRepository.save(notice);
        }
        for (int i = 11; i < 15; i++) {
            Notice notice = Notice.builder()
                    .title("공지사항(%d) 제목".formatted(i))
                    .content("공지사항(%d) 내용".formatted(i))
                    .image("img")
                    .status(Status.VISIBLE)
                    .noticeType(NoticeType.POLICY)
                    .deviceOSFilter(DeviceOSFilter.IOS)
                    .memberIdFilter(MemberIdFilter.EVEN)
                    .build();
            noticeRepository.save(notice);
        }
        for (int i = 15; i < 20; i++) {
            Notice notice = Notice.builder()
                    .title("공지사항(%d) 제목".formatted(i))
                    .content("공지사항(%d) 내용".formatted(i))
                    .image("img")
                    .status(Status.VISIBLE)
                    .noticeType(NoticeType.SERVICE)
                    .deviceOSFilter(DeviceOSFilter.ANDROID)
                    .memberIdFilter(MemberIdFilter.ODD)
                    .build();
            noticeRepository.save(notice);
        }
        for (int i = 21; i < 25; i++) {
            Notice notice = Notice.builder()
                    .title("공지사항(%d) 제목".formatted(i))
                    .content("공지사항(%d) 내용".formatted(i))
                    .image("img")
                    .status(Status.VISIBLE)
                    .noticeType(NoticeType.UPDATE)
                    .deviceOSFilter(DeviceOSFilter.IOS)
                    .memberIdFilter(MemberIdFilter.EVEN)
                    .build();
            noticeRepository.save(notice);
        }
        for (int i = 25; i < 30; i++) {
            Notice notice = Notice.builder()
                    .title("공지사항(%d) 제목".formatted(i))
                    .content("공지사항(%d) 내용".formatted(i))
                    .image("img")
                    .status(Status.VISIBLE)
                    .noticeType(NoticeType.UPDATE)
                    .deviceOSFilter(DeviceOSFilter.ANDROID)
                    .memberIdFilter(MemberIdFilter.ODD)
                    .build();
            noticeRepository.save(notice);
        }
    }
}
