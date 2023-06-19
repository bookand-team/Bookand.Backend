package kr.co.bookand.backend.util.dummy

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.account.domain.KotlinSocialType
import kr.co.bookand.backend.account.domain.dto.KotlinMiddleAccount
import kr.co.bookand.backend.account.repository.KotlinAccountRepository
import kr.co.bookand.backend.account.service.KotlinAuthService
import kr.co.bookand.backend.bookmark.domain.KotlinBookmark
import kr.co.bookand.backend.bookmark.repository.KotlinBookmarkRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Component
class KotlinAccountDummyData(
    private val authService: KotlinAuthService,
    private val accountRepository: KotlinAccountRepository,
    private val bookmarkRepository: KotlinBookmarkRepository,
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

        val middleAccount = KotlinMiddleAccount(
            email = "email",
            providerEmail = "providerEmail",
            socialType = KotlinSocialType.GOOGLE,
        )
        val account = middleAccount.toAdminAccount(passwordEncoder, adminPassword)
        val saveAccount = accountRepository.save(account)

        val initBookmark = createInitBookmark(saveAccount).toMutableList()
        saveAccount.updateBookmarks(initBookmark)
    }

    private fun createInitBookmark(saveAccount: KotlinAccount): List<KotlinBookmark> {
        return authService.getBookmarks(saveAccount, INIT_BOOKMARK_FOLDER_NAME, bookmarkRepository)
    }
}