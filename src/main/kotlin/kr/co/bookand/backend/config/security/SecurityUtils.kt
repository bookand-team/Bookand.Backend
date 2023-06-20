package kr.co.bookand.backend.config.security

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.account.repository.AccountRepository
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.exception.BookandException
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {
    fun getCurrentAccountEmail(): String =
        SecurityContextHolder.getContext().authentication?.name
            ?: throw BookandException(ErrorCode.NOT_FOUND_MEMBER)

    fun getCurrentAccount(accountRepository: AccountRepository): Account =
        accountRepository.findByEmail(getCurrentAccountEmail())
            ?: throw BookandException(ErrorCode.NOT_FOUND_MEMBER)
}
