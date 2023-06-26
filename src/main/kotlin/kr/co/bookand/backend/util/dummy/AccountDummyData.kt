package kr.co.bookand.backend.util.dummy

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.account.model.SocialType
import kr.co.bookand.backend.account.dto.MiddleAccount
import kr.co.bookand.backend.account.repository.AccountRepository
import kr.co.bookand.backend.account.service.AuthService
import kr.co.bookand.backend.bookmark.model.Bookmark
import kr.co.bookand.backend.bookmark.repository.BookmarkRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Component
class AccountDummyData(
    private val authService: AuthService,
    private val accountRepository: AccountRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${admin.secret}")
    private val adminPassword: CharSequence,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)
    companion object {
        private const val INIT_BOOKMARK_FOLDER_NAME = "모아보기"
    }

    @Transactional
    @PostConstruct
    fun dummyData() {
        if (accountRepository.count() > 0) {
            log.info("[0] 어드민, 유저가 이미 존재하여 더미를 생성하지 않았습니다.")
            return
        }

        val middleAccount = MiddleAccount(
            email = "admin",
            providerEmail = "providerEmail",
            socialType = SocialType.GOOGLE,
        )
        val account = middleAccount.toAdminAccount(passwordEncoder, adminPassword)
        val saveAccount = accountRepository.save(account)

        val initBookmark = createInitBookmark(saveAccount).toMutableList()
        saveAccount.updateBookmarks(initBookmark)
    }

    private fun createInitBookmark(saveAccount: Account): List<Bookmark> {
        return authService.getBookmarks(saveAccount, INIT_BOOKMARK_FOLDER_NAME, bookmarkRepository)
    }
}