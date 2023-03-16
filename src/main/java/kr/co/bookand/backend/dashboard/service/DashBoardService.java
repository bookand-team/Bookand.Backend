package kr.co.bookand.backend.dashboard.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.article.domain.ArticleCategory;
import kr.co.bookand.backend.article.repository.ArticleRepository;
import kr.co.bookand.backend.bookmark.domain.BookmarkType;
import kr.co.bookand.backend.bookmark.repository.BookmarkRepository;
import kr.co.bookand.backend.bookstore.repository.BookStoreRepository;
import kr.co.bookand.backend.bookstore.repository.ReportBookStoreRepository;
import kr.co.bookand.backend.common.domain.Status;
import kr.co.bookand.backend.dashboard.domain.DashBoard;
import kr.co.bookand.backend.dashboard.domain.dto.DashBoardDto.StatusBoardResponse;
import kr.co.bookand.backend.dashboard.repository.DashBoardRepository;
import kr.co.bookand.backend.feedback.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class DashBoardService {
    private final AccountService accountService;
    private final DashBoardRepository dashBoardRepository;
    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BookStoreRepository bookStoreRepository;
    private final ReportBookStoreRepository reportBookStoreRepository;
    private final FeedbackRepository feedbackRepository;

    public StatusBoardResponse getStatusBoard() {
        Account account = accountService.getCurrentAccount();
        account.getRole().checkNotUser();
        return StatusBoardResponse.of(dashBoardRepository.findById(dashBoardRepository.count()).get());
    }

    public boolean updateStatusBoard() {
        Long totalAccountNum = accountRepository.countAllByVisibility(true);
        Long totalRevokeAccountNum = accountRepository.countAllByVisibility(false);
        String startDatetime = String.valueOf(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0, 0)));
        String endDatetime = String.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)));
        String lastMonthStartDatetime = String.valueOf(LocalDateTime.of(LocalDate.now().minusMonths(1).withDayOfMonth(1), LocalTime.of(0, 0, 0)));
        String lastMonthEndDatetime = String.valueOf(LocalDateTime.of(LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth()), LocalTime.of(23, 59, 59)));

        DashBoard dashBoard = DashBoard.updateDashBoard(
                articleRepository.countAllByVisibility(true),
                articleRepository.countAllByVisibilityAndCategory(true, ArticleCategory.BOOK_REVIEW),
                articleRepository.countAllByVisibilityAndCategory(true, ArticleCategory.BOOKSTORE_REVIEW),
                articleRepository.countAllByVisibilityAndCategory(true, ArticleCategory.INTERVIEW),

                bookStoreRepository.countAllByVisibility(true), // api 등록 서점 수
                bookStoreRepository.countAllByVisibilityAndStatus(true, Status.VISIBLE),

                bookmarkRepository.countAllByVisibility(true),
                bookmarkRepository.countAllByVisibilityAndBookmarkType(true, BookmarkType.BOOKSTORE),
                bookmarkRepository.countAllByVisibilityAndBookmarkType(true, BookmarkType.BOOKSTORE) / totalAccountNum,
                bookmarkRepository.countAllByVisibilityAndBookmarkType(true, BookmarkType.ARTICLE),
                bookmarkRepository.countAllByVisibilityAndBookmarkType(true, BookmarkType.ARTICLE) / totalAccountNum,

                //TODO 유저 디바이스 정보
                totalAccountNum,

                totalRevokeAccountNum,

                reportBookStoreRepository.countAllByVisibilityAndCreatedAtBetween(true, startDatetime, endDatetime),
                reportBookStoreRepository.countAllByVisibilityAndIsAnswered(true, false),
                feedbackRepository.countAllByVisibility(true),
                feedbackRepository.countAllByVisibilityAndCreatedAtBetween(true, startDatetime, endDatetime),
                feedbackRepository.countAllByVisibilityAndCreatedAtBetween(true, lastMonthStartDatetime, lastMonthEndDatetime)
        );
        dashBoardRepository.save(dashBoard);
        return true;
    }
}


