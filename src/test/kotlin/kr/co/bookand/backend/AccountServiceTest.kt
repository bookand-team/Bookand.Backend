package kr.co.bookand.backend

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.model.*
import kr.co.bookand.backend.account.dto.AccountRequest
import kr.co.bookand.backend.account.repository.AccountRepository
import kr.co.bookand.backend.account.repository.RevokeAccountRepository
import kr.co.bookand.backend.account.repository.SuspendedAccountRepository
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.account.service.AuthService
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.config.jwt.RefreshTokenRepository
import java.time.LocalDateTime
import java.util.*

class AccountServiceTest : BehaviorSpec({
    val accountRepository = mockk<AccountRepository>()
    val authService = mockk<AuthService>()
    val suspendedAccountRepository = mockk<SuspendedAccountRepository>()
    val revokeAccountRepository = mockk<RevokeAccountRepository>()
    val refreshTokenRepository = mockk<RefreshTokenRepository>()

    val accountService = AccountService(
        accountRepository = accountRepository,
        authService = authService,
        suspendedAccountRepository = suspendedAccountRepository,
        refreshTokenRepository = refreshTokenRepository,
        revokeAccountRepository = revokeAccountRepository
    )

    Given("account Test") {

        val account = Account(
            1L,
            "email@email.com",
            "password",
            "nickname",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Role.USER,
            AccountStatus.NORMAL
        )

        val adminAccount = Account(
            2L,
            "admin@email.com",
            "password",
            "admin",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Role.ADMIN,
            AccountStatus.NORMAL
        )

        When("Admin 체크") {
            every { accountRepository.findByEmail(adminAccount.email) } returns adminAccount

            Then("it should return true") {
                accountService.isAdmin(adminAccount.email) shouldBe true
            }
        }

        When("User 체크") {
            every { accountRepository.findByEmail(account.email) } returns account

            Then("it should return true") {
                accountService.isUser(account.email) shouldBe true
            }
        }

        When("account 프로필 업데이트") {
            val request = AccountRequest(
                "nickname",
                "profileImage"
            )

            val request2 = AccountRequest(
                "nickname22",
                "profileImage"
            )
            every { accountRepository.findById(any()) } returns Optional.of(account)
            every { accountRepository.findByNickname(request.nickname) } returns account

            When("nickname Duplicate error") {
                every { accountRepository.existsByNickname(request.nickname) } returns true
                val exception = shouldThrow<RuntimeException> {
                    accountService.updateAccount(account, request)
                }

                Then("Nickname update") {
                    exception.message shouldBe ErrorCode.NICKNAME_DUPLICATION.errorLog
                }
            }

            When("success - update account") {
                every { accountRepository.findByEmail(any()) } returns account
                every { accountRepository.findById(any()) } returns Optional.of(account)
                every { accountRepository.existsByNickname(request2.nickname) } returns false
                val updatedAccount = accountService.updateAccount(account, request2)

                Then("it should update account") {

                    updatedAccount.nickname shouldBe request2.nickname
                    updatedAccount.profileImage shouldBe request2.profileImage
                }
            }
        }


        When("account 조회 (id)") {
            every { accountRepository.findById(account.id) } returns Optional.of(account)
            val findAccount = accountService.getAccountById(account.id)

            Then("it should return the account with the given id") {
                findAccount.id shouldBe account.id
            }
        }

        When("account 조회 (닉네임)") {
            every { accountRepository.findByNickname(account.nickname) } returns account
            val findAccount = accountService.getAccountByNickname(account.nickname)

            Then("it should return the account with the given nickname") {
                findAccount.id shouldBe account.id
            }
        }

    }
})