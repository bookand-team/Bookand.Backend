package kr.co.bookand.backend.config.security

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.account.repository.KotlinAccountRepository
import org.springframework.security.core.context.SecurityContextHolder

object KotlinSecurityUtils {
    fun getCurrentAccountEmail(): String =
        SecurityContextHolder.getContext().authentication?.name
            ?: throw RuntimeException("ErrorCode.NOT_FOUND_MEMBER")

    fun getCurrentAccount(accountRepository: KotlinAccountRepository): KotlinAccount =
        accountRepository.findByEmail(getCurrentAccountEmail())
            ?: throw RuntimeException("ErrorCode.NOT_FOUND_MEMBER")
}
