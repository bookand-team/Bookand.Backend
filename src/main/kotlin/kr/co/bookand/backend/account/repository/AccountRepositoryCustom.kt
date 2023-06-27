package kr.co.bookand.backend.account.repository

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.account.model.AccountStatus
import kr.co.bookand.backend.account.model.Role
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface AccountRepositoryCustom {

    fun findAllByFilter(
        role: Role?,
        accountStatus: AccountStatus?,
        pageable: Pageable
    ): Page<Account>
}