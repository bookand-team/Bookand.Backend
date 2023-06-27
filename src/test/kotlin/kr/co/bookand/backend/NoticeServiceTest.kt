package kr.co.bookand.backend

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.model.*
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.MemberIdFilter
import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.notice.model.Notice
import kr.co.bookand.backend.notice.dto.CreateNoticeRequest
import kr.co.bookand.backend.notice.dto.NoticeFilter
import kr.co.bookand.backend.notice.model.NoticeType
import kr.co.bookand.backend.notice.dto.UpdateNoticeRequest
import kr.co.bookand.backend.notice.repository.NoticeRepository
import kr.co.bookand.backend.notice.service.NoticeService
import java.time.LocalDateTime
import java.util.*

class NoticeServiceTest : BehaviorSpec({
    val noticeRepository = mockk<NoticeRepository>()
    val accountService = mockk<AccountService>()
    val noticeService = NoticeService(
        noticeRepository = noticeRepository
    )


    Given("notice service") {

        val account = Account(
            1L,
            "email",
            "password",
            "name",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
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
            null,
            Role.ADMIN,
            AccountStatus.NORMAL
        )

        val createNoticeRequest = CreateNoticeRequest(
            title = "title",
            content = "content",
            image = "image",
            noticeType = "SERVICE",
            noticeFilter = NoticeFilter(
                deviceOS = DeviceOSFilter.ALL,
                memberId = MemberIdFilter.ALL
            )
        )

        val notice = Notice(
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
            noticeFilter = NoticeFilter(
                deviceOS = DeviceOSFilter.ALL,
                memberId = MemberIdFilter.ALL
            )
        )


        When("create notice") {
            When("fail - not admin") {
                val exception = shouldThrow<Exception> {
                    noticeService.createNotice(account, createNoticeRequest)
                }

                Then("throw exception") {
                    exception.message shouldBe ErrorCode.ROLE_ACCESS_ERROR.errorLog
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
                        exception.message shouldBe ErrorCode.ROLE_ACCESS_ERROR.errorLog
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
                        exception.message shouldBe ErrorCode.ROLE_ACCESS_ERROR.errorLog
                    }
                }

                When("success") {
                    every { accountService.checkAccountAdmin(any()) } returns Unit
                    every { noticeRepository.findById(any()) } returns Optional.of(notice)
                    val noticeMessageResponse = noticeService.updateNoticeStatus(adminAccount, 1L)

                    Then("return notice id") {
                        noticeMessageResponse.id shouldBe 1L
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
                    exception.message shouldBe ErrorCode.ROLE_ACCESS_ERROR.errorLog
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