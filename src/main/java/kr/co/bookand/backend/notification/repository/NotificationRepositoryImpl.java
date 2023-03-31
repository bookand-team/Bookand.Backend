package kr.co.bookand.backend.notification.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.bookand.backend.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static kr.co.bookand.backend.notification.domain.QNotification.notification;

@Slf4j
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Notification> findAllByVisibility(Pageable pageable, boolean visibility, Long cursorId) {
        JPAQuery<Notification> query = queryFactory
                .selectFrom(notification)
                .where(
                        notification.visibility.eq(visibility),
                        getCursorId(cursorId)
                )
                .orderBy(notification.createdAt.desc(), notification.id.desc());

        return PageableExecutionUtils.getPage(
                query.orderBy(notification.id.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch(),
                pageable,
                () -> queryFactory.selectFrom(notification)
                        .fetch().size());
    }

    private BooleanExpression getCursorId(Long cursorId) {
        return (cursorId == null || cursorId == 0) ? null : notification.id.lt(cursorId);
    }
}
