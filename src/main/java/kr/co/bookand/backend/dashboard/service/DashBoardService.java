package kr.co.bookand.backend.dashboard.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.exception.NotFoundUserInformationException;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.account.util.SecurityUtil;
import kr.co.bookand.backend.article.domain.Category;
import kr.co.bookand.backend.article.repository.ArticleRepository;
import kr.co.bookand.backend.bookmark.BookMarkType;
import kr.co.bookand.backend.bookmark.repository.BookMarkRepository;
import kr.co.bookand.backend.bookstore.repository.BookStoreRepository;
import kr.co.bookand.backend.bookstore.repository.ReportBookStoreRepository;
import kr.co.bookand.backend.common.DeviceType;
import kr.co.bookand.backend.common.Status;
import kr.co.bookand.backend.dashboard.domain.DashBoard;
import kr.co.bookand.backend.dashboard.domain.dto.DashBoardDto.*;
import kr.co.bookand.backend.dashboard.repository.DashBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class DashBoardService {
    private final DashBoardRepository dashBoardRepository;
    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final BookMarkRepository bookMarkRepository;
    private final BookStoreRepository bookStoreRepository;
    private final ReportBookStoreRepository reportBookStoreRepository;

    public StatusBoardResponse getStatusBoard() {
        Account account = accountRepository.findByEmail(SecurityUtil.getCurrentAccountEmail())
                .orElseThrow(() -> new NotFoundUserInformationException(SecurityUtil.getCurrentAccountEmail()));
        account.getRole().checkNotUser();
        return StatusBoardResponse.of(dashBoardRepository.findById(dashBoardRepository.count()).get());
    }

    public boolean updateStatusBoard() {
        Long totalAccountNum = accountRepository.countBy();
        String startDatetime = String.valueOf(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0, 0)));
        String endDatetime = String.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)));

        DashBoard dashBoard = DashBoard.updateDashBoard(
                articleRepository.countBy(),
                articleRepository.countByCategory(Category.BOOK),
                articleRepository.countByCategory(Category.BOOKSTORE),
                articleRepository.countByCategory(Category.INTERVIEW),

                bookStoreRepository.countBy(),
                bookStoreRepository.countByStatus(Status.VISIBLE),

                bookMarkRepository.countBy(),
                bookMarkRepository.countByBookMarkType(BookMarkType.BOOKSTORE),
                bookMarkRepository.countByBookMarkType(BookMarkType.BOOKSTORE) / totalAccountNum,
                bookMarkRepository.countByBookMarkType(BookMarkType.ARTICLE),
                bookMarkRepository.countByBookMarkType(BookMarkType.ARTICLE) / totalAccountNum,

                //TODO 유저 디바이스 정보, 탈퇴, 제보/피드백 구현 후 수정
                totalAccountNum,
                accountRepository.countByDeviceType(DeviceType.ANDROID),
                accountRepository.countByDeviceType(DeviceType.IOS),

                totalAccountNum,
                totalAccountNum,
                totalAccountNum,

                reportBookStoreRepository.countByCreatedAtBetween(startDatetime, endDatetime),
                reportBookStoreRepository.countByStatus(Status.INVISIBLE),
                totalAccountNum,
                totalAccountNum,
                totalAccountNum
        );
        dashBoardRepository.save(dashBoard);
        return true;
    }
}


