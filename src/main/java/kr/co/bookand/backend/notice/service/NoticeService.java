package kr.co.bookand.backend.notice.service;

import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import kr.co.bookand.backend.common.exception.ErrorCode;
import kr.co.bookand.backend.notice.domain.Notice;
import kr.co.bookand.backend.notice.domain.dto.NoticeDto.*;
import kr.co.bookand.backend.notice.exception.NoticeException;
import kr.co.bookand.backend.notice.repository.NoticeRepository;
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
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AccountService accountService;

    public PageResponse<NoticeResponse> getNoticeList(Pageable pageable, Long cursorId) {
        Page<NoticeResponse> notificationList = noticeRepository.findAllByVisibility(pageable, true, cursorId)
                .map(NoticeResponse::of);
        return PageResponse.of(notificationList);
    }

    @Transactional
    public NoticeResponse createNotice(CreateNoticeRequest createNoticeRequest) {
        accountService.isAccountAdmin();
        Notice notice = createNoticeRequest.toEntity();
        noticeRepository.save(notice);
        return NoticeResponse.of(notice);
    }

    public NoticeResponse getNotice(Long notificationId) {
        Notice notice = noticeRepository.findById(notificationId)
                .orElseThrow(() -> new NoticeException(ErrorCode.NOT_FOUND_NOTIFICATION, notificationId));
        return NoticeResponse.of(notice);
    }
}
