package kr.co.bookand.backend.config.security

import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.account.repository.AccountRepository
import lombok.RequiredArgsConstructor
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class PrincipalDetailService(
    private val accountRepository: AccountRepository
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val account = accountRepository.findByEmail(email)
            ?: throw RuntimeException("ErrorCode.NOT_FOUND_MEMBER")
        return createUserDetails(account)
    }

    private fun createUserDetails(account: Account): UserDetails {
        return PrincipalDetails(account)
    }

}