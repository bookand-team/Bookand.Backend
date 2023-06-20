package kr.co.bookand.backend.bookstore.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import kr.co.bookand.backend.bookstore.domain.KotlinBookstoreType
import kr.co.bookand.backend.bookstore.domain.QKotlinBookstore
import kr.co.bookand.backend.bookstore.domain.QKotlinBookstore.*
import kr.co.bookand.backend.common.KotlinStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

class KotlinBookstoreRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : KotlinBookstoreRepositoryCustom {
    override fun findAllBySearch(
        account: KotlinAccount,
        search: String?,
        theme: String?,
        status: String?,
        pageable: Pageable
    ): Page<KotlinBookstore> {
        val query = queryFactory.selectFrom(kotlinBookstore)
            .where(containSearch(search))
            .where(eqTheme(theme))
            .where(eqStatus(status))
            .orderBy(kotlinBookstore.id.desc())
        return PageableExecutionUtils.getPage(
            query.offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch(),
            pageable,
            query::fetchCount
        )
    }

    private fun containSearch(search: String?): BooleanExpression? {
        if (search.isNullOrEmpty()) {
            return null
        }
        return kotlinBookstore.name.contains(search)
    }

    private fun eqTheme(theme: String?): BooleanExpression? {
        if (theme.isNullOrEmpty()) {
            return null
        }
        val bookStoreType = KotlinBookstoreType.valueOf(theme)
        return kotlinBookstore.themeList.any().theme.eq(bookStoreType)
    }

    private fun eqStatus(status: String?): BooleanExpression? {
        if (status.isNullOrEmpty()) {
            return null
        }
        val statusType = KotlinStatus.valueOf(status)
        return kotlinBookstore.status.eq(statusType)
    }

}