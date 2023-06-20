package kr.co.bookand.backend.notice.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.common.KotlinStatus
import kr.co.bookand.backend.notice.domain.KotlinNotice
import kr.co.bookand.backend.notice.domain.QKotlinNotice
import kr.co.bookand.backend.notice.domain.QKotlinNotice.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

class KotlinNoticeRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : KotlinNoticeRepositoryCustom {
    override fun findAllByVisibilityAndStatus(
        pageable: Pageable,
        visibility: Boolean,
        status: KotlinStatus,
        cursorId: Long?
    ): Page<KotlinNotice> {

        val query = queryFactory.selectFrom(kotlinNotice)
            .where(
                kotlinNotice.visibility.eq(visibility),
                kotlinNotice.status.eq(status),
                getCursorId(cursorId)
            )
            .orderBy(kotlinNotice.createdAt.desc(), kotlinNotice.id.desc())
        return PageableExecutionUtils.getPage(
            query.orderBy(kotlinNotice.id.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch(),
            pageable,
            query::fetchCount
        )
    }

    private fun getCursorId(cursorId: Long?): BooleanExpression? {
        return if (cursorId == null || cursorId == 0L) null else kotlinNotice.id.lt(cursorId)
    }
}