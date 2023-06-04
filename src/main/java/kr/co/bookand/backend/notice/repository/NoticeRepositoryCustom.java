package kr.co.bookand.backend.notice.repository;

import kr.co.bookand.backend.common.domain.Status;
import kr.co.bookand.backend.notice.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<Notice> findAllByVisibilityAndStatus(Pageable pageable, boolean visibility, Status status, Long cursorId);
}
