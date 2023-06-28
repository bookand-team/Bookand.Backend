package kr.co.bookand.backend.account.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.account.model.AccountStatus
import kr.co.bookand.backend.account.model.QAccount.account
import kr.co.bookand.backend.account.model.Role
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

class AccountRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): AccountRepositoryCustom {
    override fun findAllByFilter(
        role: Role?,
        accountStatus: AccountStatus?,
        pageable: Pageable
    ): Page<Account> {
        val query = queryFactory.selectFrom(account)
            .where(eqRole(role))
            .where(eqAccountStatus(accountStatus))
            .orderBy(account.id.asc())
        return PageableExecutionUtils.getPage(
            query.offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch(),
            pageable,
            query::fetchCount)

    }

    private fun eqRole(role: Role?): BooleanExpression? {
        if (role == null) {
            return null
        }
        return account.role.eq(role)
    }

    private fun eqAccountStatus(accountStatus: AccountStatus?): BooleanExpression? {
        if (accountStatus == null) {
            return null
        }
        return account.accountStatus.eq(accountStatus)
    }
}