package kr.co.bookand.backend.bookstore.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.bookstore.domain.Bookstore
import kr.co.bookand.backend.bookstore.domain.BookstoreType
import kr.co.bookand.backend.bookstore.domain.QBookstore.*
import kr.co.bookand.backend.common.Status
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

class BookstoreRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : BookstoreRepositoryCustom {
    override fun findAllBySearch(
        account: Account,
        search: String?,
        theme: String?,
        status: String?,
        pageable: Pageable
    ): Page<Bookstore> {
        val query = queryFactory.selectFrom(bookstore)
            .where(containSearch(search))
            .where(eqTheme(theme))
            .where(eqStatus(status))
            .orderBy(bookstore.id.desc())
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
        return bookstore.name.contains(search)
    }

    private fun eqTheme(theme: String?): BooleanExpression? {
        if (theme.isNullOrEmpty()) {
            return null
        }
        val bookStoreType = BookstoreType.valueOf(theme)
        return bookstore.themeList.any().theme.eq(bookStoreType)
    }

    private fun eqStatus(status: String?): BooleanExpression? {
        if (status.isNullOrEmpty()) {
            return null
        }
        val statusType = Status.valueOf(status)
        return bookstore.status.eq(statusType)
    }

}