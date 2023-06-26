package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.bookstore.model.Bookstore
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BookstoreRepositoryCustom {
    fun findAllBySearch(
        account: Account,
        search: String?,
        theme: String?,
        status: String?,
        pageable: Pageable
    ): Page<Bookstore>
}