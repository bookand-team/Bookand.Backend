package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.bookstore.domain.BookStore
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface KotlinBookstoreRepositoryCustom {
    fun findAllBySearch(
        account: KotlinAccount,
        search: String?,
        theme: String?,
        status: String?,
        pageable: Pageable?
    ): Page<KotlinBookstore>
}