package kr.co.bookand.backend.common.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.SocialType;
import kr.co.bookand.backend.account.domain.dto.AuthDto;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.bookmark.domain.Bookmark;
import kr.co.bookand.backend.bookmark.domain.BookmarkType;
import kr.co.bookand.backend.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountDummyData {

    private final AccountRepository accountRepository;
    private final BookmarkRepository bookmarkRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${admin.secret}")
    private CharSequence ADMIN_PASSWORD;

    private static final String INIT_BOOKMARK_FOLDER_NAME = "모아보기";

    @Transactional
    @PostConstruct
    public void dummyData() {
        AuthDto.MiddleAccount middleAccount = AuthDto.MiddleAccount.builder()
                .email("email")
                .socialType(SocialType.GOOGLE)
                .providerEmail("providerEmail")
                .build();
        Account account = middleAccount.toAdmin(passwordEncoder, ADMIN_PASSWORD);
        Account saveAccount = accountRepository.save(account);

        List<Bookmark> initBookmark = createInitBookmark(saveAccount);
        saveAccount.updateBookmarkList(initBookmark);
    }

    private List<Bookmark> createInitBookmark(Account saveAccount) {
        Bookmark initBookmarkArticle = Bookmark.builder()
                .account(saveAccount)
                .folderName(INIT_BOOKMARK_FOLDER_NAME)
                .bookmarkType(BookmarkType.ARTICLE)
                .build();
        Bookmark initBookmarkBookStore = Bookmark.builder()
                .account(saveAccount)
                .folderName(INIT_BOOKMARK_FOLDER_NAME)
                .bookmarkType(BookmarkType.BOOKSTORE)
                .build();

        bookmarkRepository.save(initBookmarkArticle);
        bookmarkRepository.save(initBookmarkBookStore);
        return Arrays.asList(initBookmarkArticle, initBookmarkBookStore);
    }
}
