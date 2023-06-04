package kr.co.bookand.backend.notice.repository;

import kr.co.bookand.backend.notice.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
    Page<Notice> findAllByVisibility(Pageable pageable, boolean visibility, Long cursorId);
}
