package kr.co.bookand.backend.bookstore.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class KotlinBookstoreRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : KotlinBookstoreRepositoryCustom {
    override fun findAllBySearch(
        account: KotlinAccount,
        search: String?,
        theme: String?,
        status: String?,
        pageable: Pageable?
    ): Page<KotlinBookstore> {
        TODO("Not yet implemented")
    }

}