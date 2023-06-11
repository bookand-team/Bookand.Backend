package kr.co.bookand.backend

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.domain.AccountStatus
import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.account.domain.Role
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.common.domain.DeviceOSFilter
import kr.co.bookand.backend.common.domain.MemberIdFilter
import kr.co.bookand.backend.common.domain.Status
import kr.co.bookand.backend.notice.domain.KotlinNotice
import kr.co.bookand.backend.notice.domain.NoticeType
import kr.co.bookand.backend.notice.domain.dto.CreateNoticeRequest
import kr.co.bookand.backend.notice.domain.dto.UpdateNoticeRequest
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


    given("notice service") {

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

        val createNoticeRequest = CreateNoticeRequest(
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
            Status.INVISIBLE,
            NoticeType.SERVICE,
            DeviceOSFilter.ALL,
            MemberIdFilter.ALL
        )

        val updateNoticeRequest = UpdateNoticeRequest(
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
                    noticeService.createNotice(1L, createNoticeRequest)
                }

                Then("throw exception") {
                    exception.message shouldBe "관리자가 아닙니다."
                }
            }

            When("success") {
                every { accountService.checkAccountAdmin(any()) } returns Unit
                every { noticeRepository.save(any()) } returns notice
                val noticeIdResponse = noticeService.createNotice(1L, createNoticeRequest)

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
                        noticeService.updateNotice(1L, 1L, updateNoticeRequest)
                    }

                    Then("throw exception") {
                        exception.message shouldBe "관리자가 아닙니다."
                    }
                }

                When("success") {
                    every { accountService.checkAccountAdmin(any()) } returns Unit
                    every { noticeRepository.findById(any()) } returns Optional.of(notice)
                    val noticeIdResponse = noticeService.updateNotice(1L, 1L, updateNoticeRequest)

                    Then("return notice id") {
                        noticeIdResponse.id shouldBe 1L
                    }
                }

            }

            When("update notice status"){
                When("fail - not admin") {
                    every { accountService.checkAccountAdmin(any()) } throws Exception("관리자가 아닙니다.")
                    val exception = shouldThrow<Exception> {
                        noticeService.updateNoticeStatus(1L, 1L, Status.VISIBLE)
                    }

                    Then("throw exception") {
                        exception.message shouldBe "관리자가 아닙니다."
                    }
                }

                When("success") {
                    every { accountService.checkAccountAdmin(any()) } returns Unit
                    every { noticeRepository.findById(any()) } returns Optional.of(notice)
                    val noticeMessageResponse = noticeService.updateNoticeStatus(1L, 1L, Status.VISIBLE)

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
                    noticeService.deleteNotice(1L, 1L)
                }

                Then("throw exception") {
                    exception.message shouldBe "관리자가 아닙니다."
                }
            }

            When("success") {
                every { accountService.checkAccountAdmin(any()) } returns Unit
                every { noticeRepository.findById(any()) } returns Optional.of(notice)
                every { noticeRepository.deleteById(any()) } returns Unit

                val noticeMessageResponse = noticeService.deleteNotice(1L, 1L)

                Then("return notice id") {
                    noticeMessageResponse.id shouldBe 1L
                }
            }

        }
    }
})