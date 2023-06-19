package kr.co.bookand.backend

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.domain.*
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.common.KotlinDeviceOSFilter
import kr.co.bookand.backend.common.KotlinMemberIdFilter
import kr.co.bookand.backend.common.KotlinStatus
import kr.co.bookand.backend.notice.domain.KotlinNotice
import kr.co.bookand.backend.notice.domain.dto.KotlinCreateNoticeRequest
import kr.co.bookand.backend.notice.domain.dto.KotlinNoticeType
import kr.co.bookand.backend.notice.domain.dto.KotlinUpdateNoticeRequest
import kr.co.bookand.backend.notice.repository.KotlinNoticeRepository
import kr.co.bookand.backend.notice.service.KotlinNoticeService
import java.time.LocalDateTime
import java.util.*

class NoticeServiceTest : BehaviorSpec({
    val noticeRepository = mockk<KotlinNoticeRepository>()
    val accountService = mockk<KotlinAccountService>()
    val noticeService = KotlinNoticeService(
        noticeRepository = noticeRepository,
        accountService = accountService
    )


    Given("notice service") {

        val account = KotlinAccount(
            1L,
            "email",
            "password",
            "name",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            KotlinRole.USER,
            KotlinAccountStatus.NORMAL
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
            KotlinRole.ADMIN,
            KotlinAccountStatus.NORMAL
        )

        val createNoticeRequest = KotlinCreateNoticeRequest(
            title = "title",
            content = "content",
            image = "image",
            noticeType = "SERVICE",
            targetType = "ALL",
            targetNum = "ALL"
        )

        val notice = KotlinNotice(
            1L,
            "title",
            "content",
            "image",
            KotlinStatus.INVISIBLE,
            KotlinNoticeType.SERVICE,
            KotlinDeviceOSFilter.ALL,
            KotlinMemberIdFilter.ALL
        )

        val updateNoticeRequest = KotlinUpdateNoticeRequest(
            title = "title",
            content = "content",
            image = "image",
            noticeType = "SERVICE",
            targetType = "ALL",
            targetNum = "ALL"
        )


        When("create notice") {
            When("fail - not admin") {
                every { accountService.checkAccountAdmin(any()) } throws Exception("관리자가 아닙니다.")
                val exception = shouldThrow<Exception> {
                    noticeService.createNotice(adminAccount, createNoticeRequest)
                }

                Then("throw exception") {
                    exception.message shouldBe "관리자가 아닙니다."
                }
            }

            When("success") {
                every { accountService.checkAccountAdmin(any()) } returns Unit
                every { noticeRepository.save(any()) } returns notice
                val noticeIdResponse = noticeService.createNotice(adminAccount, createNoticeRequest)

                Then("return notice id") {
                    noticeIdResponse.id shouldBe 1L
                }
            }
        }
        When("update notice") {

            When("update notice info"){
                When("fail - not admin") {
                    every { accountService.checkAccountAdmin(any()) } throws Exception("관리자가 아닙니다.")
                    val exception = shouldThrow<Exception> {
                        noticeService.updateNotice(account, 1L, updateNoticeRequest)
                    }

                    Then("throw exception") {
                        exception.message shouldBe "관리자가 아닙니다."
                    }
                }

                When("success") {
                    every { accountService.checkAccountAdmin(any()) } returns Unit
                    every { noticeRepository.findById(any()) } returns Optional.of(notice)
                    val noticeIdResponse = noticeService.updateNotice(adminAccount, 1L, updateNoticeRequest)

                    Then("return notice id") {
                        noticeIdResponse.id shouldBe 1L
                    }
                }

            }

            When("update notice status"){
                When("fail - not admin") {
                    every { accountService.checkAccountAdmin(any()) } throws Exception("관리자가 아닙니다.")
                    val exception = shouldThrow<Exception> {
                        noticeService.updateNoticeStatus(account, 1L)
                    }

                    Then("throw exception") {
                        exception.message shouldBe "관리자가 아닙니다."
                    }
                }

                When("success") {
                    every { accountService.checkAccountAdmin(any()) } returns Unit
                    every { noticeRepository.findById(any()) } returns Optional.of(notice)
                    val noticeMessageResponse = noticeService.updateNoticeStatus(adminAccount, 1L)

                    Then("return notice id") {
                        noticeMessageResponse.message shouldBe "Update Status to Visible."
                    }
                }
            }

        }
        When("delete notice") {
            When("fail - not admin") {
                every { accountService.checkAccountAdmin(any()) } throws Exception("관리자가 아닙니다.")
                val exception = shouldThrow<Exception> {
                    noticeService.deleteNotice(account, 1L)
                }

                Then("throw exception") {
                    exception.message shouldBe "관리자가 아닙니다."
                }
            }

            When("success") {
                every { accountService.checkAccountAdmin(any()) } returns Unit
                every { noticeRepository.findById(any()) } returns Optional.of(notice)
                every { noticeRepository.deleteById(any()) } returns Unit

                val noticeMessageResponse = noticeService.deleteNotice(adminAccount, 1L)

                Then("return notice id") {
                    noticeMessageResponse.statusCode shouldBe 200
                }
            }

        }
    }
})