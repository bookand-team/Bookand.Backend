package kr.co.bookand.backend

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.domain.AccountStatus
import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.account.domain.Role
import kr.co.bookand.backend.account.domain.dto.KotlinAccountRequest
import kr.co.bookand.backend.account.repository.KotlinAccountRepository
import kr.co.bookand.backend.account.service.KotlinAccountService
import java.time.LocalDateTime
import java.util.*

class AccountServiceTest : BehaviorSpec({
    val accountRepository = mockk<KotlinAccountRepository>()
    val accountService = KotlinAccountService(accountRepository)

    Given("account Test"){
        val accountId = 1L
        val account = KotlinAccount(
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

        val adminAccount = KotlinAccount(
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

        When("Admin 체크"){
            every { accountRepository.findByEmail(adminAccount.email) } returns adminAccount

            Then("it should return true"){
                accountService.isAdmin(adminAccount.email) shouldBe true
            }
        }

        When("User 체크"){
            every { accountRepository.findByEmail(account.email) } returns account

            Then("it should return true"){
                accountService.isUser(account.email) shouldBe true
            }
        }

        When("account 프로필 업데이트") {
            val request = KotlinAccountRequest(
                "nickname",
                "profileImage"
            )

            val request2 = KotlinAccountRequest(
                "nickname22",
                "profileImage"
            )
            every { accountRepository.findById(account.id) } returns Optional.of(account)
            every { accountRepository.findByNickname(request.nickname) } returns account

            Then("duplicate Nickname Error") {
                every { accountRepository.existsByNickname(request.nickname) } returns true
                val exception = shouldThrow<IllegalArgumentException> {
                    accountService.updateAccount(accountId, request)
                }
                exception.message shouldBe "Nickname is already taken"
            }

            Then("it should update account") {
                every { accountRepository.findById(account.id) } returns Optional.of(account)
                every { accountRepository.existsByNickname(request2.nickname) } returns false

                val updatedAccount = accountService.updateAccount(accountId, request2)

                updatedAccount.nickname shouldBe request2.nickname
                updatedAccount.profileImage shouldBe request2.profileImage
            }
        }


        When("account 조회 (id)"){
            every { accountRepository.findById(account.id) } returns Optional.of(account)
            val findAccount = accountService.getAccountById(account.id)

            Then("it should return the account with the given id"){
                findAccount.id shouldBe account.id
            }
        }

        When("account 조회 (닉네임)"){
            every { accountRepository.findByNickname(account.nickname) } returns account
            val findAccount = accountService.getAccountByNickname(account.nickname)

            Then("it should return the account with the given nickname"){
                findAccount.id shouldBe account.id
            }
        }

    }
})