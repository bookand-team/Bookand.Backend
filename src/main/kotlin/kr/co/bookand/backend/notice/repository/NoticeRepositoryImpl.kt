package kr.co.bookand.backend.notice.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.notice.model.Notice
import kr.co.bookand.backend.notice.model.QNotice.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

class NoticeRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : NoticeRepositoryCustom {
    override fun findAllByVisibilityAndStatus(
        pageable: Pageable,
        visibility: Boolean,
        status: Status,
        cursorId: Long?
    ): Page<Notice> {

        val query = queryFactory.selectFrom(notice)
            .where(
                notice.visibility.eq(visibility),
                notice.status.eq(status),
                getCursorId(cursorId)
            )
            .orderBy(notice.createdAt.desc(), notice.id.desc())
        return PageableExecutionUtils.getPage(
            query.orderBy(notice.id.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch(),
            pageable,
            query::fetchCount
        )
    }

    private fun getCursorId(cursorId: Long?): BooleanExpression? {
        return if (cursorId == null || cursorId == 0L) null else notice.id.lt(cursorId)
    }
}