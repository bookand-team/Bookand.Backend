package kr.co.bookand.backend.dashboard.service

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.account.repository.AccountRepository
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.article.model.ArticleCategory
import kr.co.bookand.backend.article.repository.ArticleRepository
import kr.co.bookand.backend.bookmark.model.BookmarkType
import kr.co.bookand.backend.bookmark.repository.BookmarkRepository
import kr.co.bookand.backend.bookstore.repository.BookstoreRepository
import kr.co.bookand.backend.bookstore.repository.ReportBookstoreRepository
import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.dashboard.model.DashBoard
import kr.co.bookand.backend.dashboard.dto.StatusBoardResponse
import kr.co.bookand.backend.dashboard.repository.DashBoardRepository
import kr.co.bookand.backend.feedback.repository.FeedbackRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
@RequiredArgsConstructor
class DashBoardService(
    private val dashBoardRepository: DashBoardRepository,
    private val accountRepository: AccountRepository,
    private val articleRepository: ArticleRepository,
    private val bookstoreRepository: BookstoreRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val reportBookstoreRepository: ReportBookstoreRepository,
    private val feedbackRepository: FeedbackRepository
) {
    fun getStatusBoard(account: Account) : StatusBoardResponse {
        account.role.checkAdminAndManager()
        val dashBoard = dashBoardRepository.findById(dashBoardRepository.count()).get()
        return StatusBoardResponse(
            totalArticleNum = dashBoard.totalArticleNum,
            bookArticleNum = dashBoard.bookArticleNum,
            bookStoreArticleNum = dashBoard.bookStoreArticleNum,
            interviewArticleNum = dashBoard.interviewArticleNum,
            registrationBookStoreNum = dashBoard.registrationBookStoreNum,
            shownBookStoreNum = dashBoard.shownBookStoreNum,
            totalBookMarkNum = dashBoard.totalBookMarkNum,
            articleBookMarkNum = dashBoard.articleBookMarkNum,
            articleBookMarkAvg = dashBoard.articleBookMarkAvg,
            bookStoreBookMarkNum = dashBoard.bookStoreBookMarkNum,
            bookStoreBookMarkAvg = dashBoard.bookStoreBookMarkAvg,
            totalAccountNum = dashBoard.totalAccountNum,
            totalRevokeNum = dashBoard.totalRevokeNum,
            todayBookStoreNum = dashBoard.todayBookStoreNum,
            invisibleBookStoreNum = dashBoard.invisibleBookStoreNum,
            totalFeedBackNum = dashBoard.totalFeedBackNum,
            todayFeedBackNum = dashBoard.todayFeedBackNum,
            lastFeedBackNum = dashBoard.lastFeedBackNum
        )
    }

    fun updateStatusBoard() : Boolean{
        val totalAccountNum: Long = accountRepository.countAllByVisibility(true)
        val totalRevokeAccountNum: Long = accountRepository.countAllByVisibility(false)
        val startDatetime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0, 0)).toString()
        val endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)).toString()
        val lastMonthStartDatetime = LocalDateTime.of(LocalDate.now().minusMonths(1).withDayOfMonth(1), LocalTime.of(0, 0, 0)).toString()
        val lastMonthEndDatetime = LocalDateTime.of(LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth()), LocalTime.of(23, 59, 59)).toString()


        val dashBoard = DashBoard(
            articleRepository.countAllByVisibility(true),
            articleRepository.countAllByVisibilityAndCategory(true, ArticleCategory.BOOK_REVIEW),
            articleRepository.countAllByVisibilityAndCategory(true, ArticleCategory.BOOKSTORE_REVIEW),
            articleRepository.countAllByVisibilityAndCategory(true, ArticleCategory.INTERVIEW),
            bookstoreRepository.countAllByVisibility(true),  // api 등록 서점 수
            bookstoreRepository.countAllByVisibilityAndStatus(true, Status.VISIBLE),
            bookmarkRepository.countAllByVisibility(true),
            bookmarkRepository.countAllByVisibilityAndBookmarkType(true, BookmarkType.BOOKSTORE),
            bookmarkRepository.countAllByVisibilityAndBookmarkType(true, BookmarkType.BOOKSTORE) / totalAccountNum,
            bookmarkRepository.countAllByVisibilityAndBookmarkType(true, BookmarkType.ARTICLE),
            bookmarkRepository.countAllByVisibilityAndBookmarkType(true, BookmarkType.ARTICLE) / totalAccountNum,

            //TODO 유저 디바이스 정보
            totalAccountNum,

            totalRevokeAccountNum,

            reportBookstoreRepository.countAllByVisibilityAndCreatedAtBetween(true, startDatetime, endDatetime),
            reportBookstoreRepository.countAllByVisibilityAndIsAnswered(true, false),
            feedbackRepository.countAllByVisibility(true),
            feedbackRepository.countAllByVisibilityAndCreatedAtBetween(true, startDatetime, endDatetime),
            feedbackRepository.countAllByVisibilityAndCreatedAtBetween(true, lastMonthStartDatetime, lastMonthEndDatetime)
        )
        dashBoardRepository.save(dashBoard)
        return true
    }
}