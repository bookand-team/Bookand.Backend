package kr.co.bookand.backend.notice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.bookand.backend.common.domain.Status;
import kr.co.bookand.backend.notice.domain.Notice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static kr.co.bookand.backend.notice.domain.QNotice.notice;


@Slf4j
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Notice> findAllByVisibilityAndStatus(Pageable pageable, boolean visibility, Status status, Long cursorId) {
        JPAQuery<Notice> query = queryFactory
                .selectFrom(notice)
                .where(
                        notice.visibility.eq(visibility),
                        notice.status.eq(status),
                        getCursorId(cursorId)
                )
                .orderBy(notice.createdAt.desc(), notice.id.desc());

        return PageableExecutionUtils.getPage(
                query.orderBy(notice.id.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch(),
                pageable,
                () -> queryFactory.selectFrom(notice)
                        .fetch().size());
    }

    private BooleanExpression getCursorId(Long cursorId) {
        return (cursorId == null || cursorId == 0) ? null : notice.id.lt(cursorId);
    }
}
