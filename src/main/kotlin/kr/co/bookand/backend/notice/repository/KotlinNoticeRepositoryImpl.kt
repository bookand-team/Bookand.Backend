package kr.co.bookand.backend.notice.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.common.KotlinStatus
import kr.co.bookand.backend.notice.domain.KotlinNotice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class KotlinNoticeRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : KotlinNoticeRepositoryCustom {
    override fun findAllByVisibilityAndStatus(
        pageable: Pageable?,
        visibility: Boolean,
        status: KotlinStatus,
        cursorId: Long?
    ): Page<KotlinNotice> {
        TODO("Not yet implemented")
    }
}