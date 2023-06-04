package kr.co.bookand.backend.notice.service;

import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.common.domain.Status;
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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AccountService accountService;

    @Transactional
    public NoticeIdResponse createNotice(CreateNoticeRequest createNoticeRequest) {
        accountService.isAccountAdmin();
        Notice notice = createNoticeRequest.toEntity();
        noticeRepository.save(notice);
        return new NoticeIdResponse(notice.getId());
    }

    public PageResponse<NoticeResponse> getNoticeSimpleList(Pageable pageable, Long cursorId) {
        Page<NoticeResponse> notificationList = noticeRepository.findAllByVisibilityAndStatus(pageable, true, Status.VISIBLE, cursorId)
                .map(NoticeResponse::of);
        return PageResponse.of(notificationList);
    }

    public NoticeResponse getNotice(Long notificationId) {
        Notice notice = noticeRepository.findById(notificationId)
                .orElseThrow(() -> new NoticeException(ErrorCode.NOT_FOUND_NOTIFICATION, notificationId));
        return NoticeResponse.of(notice);
    }

    public PageResponse<NoticeWebResponse> getNoticeList(Pageable pageable) {
        accountService.isAccountAdmin();
        return PageResponse.of(noticeRepository.findAll(pageable).map(NoticeWebResponse::of));
    }

    @Transactional
    public NoticeIdResponse updateNotice(Long notificationId, UpdateNoticeRequest updateNoticeRequest) {
        accountService.isAccountAdmin();
        Notice notice = findNotice(notificationId);
        notice.updateNoticeData(updateNoticeRequest);
        return new NoticeIdResponse(notice.getId());
    }

    @Transactional
    public NoticeMessage deleteNotice(Long notificationId) {
        accountService.isAccountAdmin();
        Notice notice = findNotice(notificationId);
        notice.softDelete();
        notice.updateNoticeStatus(Status.REMOVE);
        return new NoticeMessage("Delete Success.");
    }

    @Transactional
    public NoticeMessage updateNoticeStatus(Long notificationId) {
        accountService.isAccountAdmin();
        Notice notice = findNotice(notificationId);
        String message;
        if (notice.getStatus().equals(Status.VISIBLE)) {
            notice.updateNoticeStatus(Status.INVISIBLE);
            message = "Update Status to Invisible.";
        } else {
            notice.updateNoticeStatus(Status.VISIBLE);
            message = "Update Status to Visible.";
        }
        notice.updateDisplayAt(LocalDateTime.now());
       return new NoticeMessage(message);
    }

    public Notice findNotice(Long notificationId) {
        Notice notice = noticeRepository.findById(notificationId)
                .orElseThrow(() -> new NoticeException(ErrorCode.NOT_FOUND_NOTIFICATION, notificationId));
        return notice;
    }
}
