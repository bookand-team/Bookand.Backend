package kr.co.bookand.backend.account.service

import kr.co.bookand.backend.account.domain.*
import kr.co.bookand.backend.account.domain.dto.*
import kr.co.bookand.backend.account.repository.AccountRepository
import kr.co.bookand.backend.account.repository.RevokeAccountRepository
import kr.co.bookand.backend.account.repository.SuspendedAccountRepository
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.common.domain.MessageResponse
import kr.co.bookand.backend.common.exception.BookandException
import kr.co.bookand.backend.config.jwt.RefreshTokenRepository
import kr.co.bookand.backend.config.security.SecurityUtils.getCurrentAccountEmail
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*


@Service
@Transactional(readOnly = true)
class AccountService(
    private val accountRepository: AccountRepository,
    private val suspendedAccountRepository: SuspendedAccountRepository,
    private val revokeAccountRepository: RevokeAccountRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val authService: AuthService
) {

    fun getCurrentAccount(): Account {
        return accountRepository.findByEmail(getCurrentAccountEmail())
            ?: throw BookandException(ErrorCode.NOT_FOUND_MEMBER)
    }

    fun getMyAccountInfo(): AccountInfoResponse {
        return AccountInfoResponse(getCurrentAccount())
    }

    fun getAccountByEmail(email: String): Account {
        return accountRepository.findByEmail(email)
            ?: throw BookandException(ErrorCode.NOT_FOUND_MEMBER)
    }

    fun getAccountByNickname(nickname: String): AccountInfoResponse {
        val account = accountRepository.findByNickname(nickname)
            ?: throw BookandException(ErrorCode.NOT_FOUND_MEMBER)
        return AccountInfoResponse(account)
    }

    fun getAccountById(id: Long): Account {
        return accountRepository.findById(id)
            .orElseThrow { BookandException(ErrorCode.NOT_FOUND_MEMBER) }
    }

    fun getAccountInfoById(id: Long): AccountInfoResponse {
        return AccountInfoResponse(getAccountById(id))
    }

    fun getAccountList(pageable: Pageable): AccountListResponse {
        val accountList = accountRepository.findAll(pageable)
            .map { AccountInfoResponse(it) }
        return AccountListResponse(PageResponse.of(accountList))
    }

    fun getRandomNickname(): NicknameResponse {
        var nicknameRandom = authService.nicknameRandom()
        while (checkNicknameBoolean(nicknameRandom, "")) {
            nicknameRandom = authService.nicknameRandom()
        }
        return NicknameResponse(nicknameRandom)
    }

    @Transactional
    fun updateAccount(currentAccount: Account, request: AccountRequest): AccountInfoResponse {
        val checkNicknameBoolean = checkNicknameBoolean(request.nickname, currentAccount.nickname)
        if (checkNicknameBoolean) {
            throw BookandException(ErrorCode.NICKNAME_DUPLICATION)
        }
        currentAccount.updateProfile(request.profileImage, request.nickname)
        return AccountInfoResponse(currentAccount)
    }

    fun existNickname(nickname: String): MessageResponse {
        val existsByNickname = accountRepository.existsByNickname(nickname)
        return if (existsByNickname) {
            MessageResponse("CONFLICT", 409)
        } else {
            MessageResponse("OK", 200)
        }
    }

    @Transactional
    fun revokeAccount(currentAccount: Account, request: RevokeReasonRequest): Boolean {
        val checkRevokeType = RevokeType.checkRevokeType(request.revokeType)
        val authRequest = AuthRequest(
            accessToken = request.socialAccessToken,
            socialType = currentAccount.provider,
        )
        checkLoginMember(currentAccount, authRequest)
        val revokeAccount = RevokeAccount(
            reason = request.reason,
            revokeType = checkRevokeType,
            accountId = currentAccount.id
        )
        revokeAccountRepository.save(revokeAccount)
        refreshTokenRepository.deleteByAccountId(currentAccount.id)
        accountRepository.delete(currentAccount)
        return true
    }

    @Transactional
    fun suspendedAccount(currentAccount: Account, accountId: Long): AccountStatus {
        checkAccountAdmin(currentAccount.id)
        val account = getAccountById(accountId)
        val suspendedAccount = getSuspendedAccount(account)
            ?: suspendedAccountRepository.save(SuspendedAccount(account = account))
        return setSuspendedAccount(account, suspendedAccount)
    }

    fun setSuspendedAccount(account: Account, suspendedAccount: SuspendedAccount): AccountStatus {
        val suspendedCount = suspendedAccount.suspendedCount

        val suspendedAt =
            if (suspendedCount == 0) LocalDateTime.now().plusDays(7)
            else LocalDateTime.now().plusMonths(6)

        val newStatus =
            if (suspendedCount == 0) AccountStatus.SUSPENDED
            else AccountStatus.DELETED

        suspendedAccount.setSuspendedAt(suspendedAt)
        suspendedAccount.addSuspendedCount()
        account.updateAccountStatus(newStatus)

        if (newStatus == AccountStatus.DELETED) account.deletedBanAccount()
        return newStatus
    }

    private fun getSuspendedAccount(account: Account): SuspendedAccount? {
        return suspendedAccountRepository.findByAccount(account)
    }


    fun isAdmin(email: String): Boolean {
        val account = accountRepository.findByEmail(email) ?: return false
        return account.role.name == "ADMIN"
    }

    fun isUser(email: String): Boolean {
        val account = accountRepository.findByEmail(email) ?: return false
        return account.role.name == "USER"
    }


    fun checkAccountAdmin(id: Long) {
        val account = accountRepository.findById(id)
            .orElseThrow { BookandException(ErrorCode.NOT_FOUND_MEMBER)}
        if (account.role.name != "ADMIN") {
            throw BookandException(ErrorCode.ROLE_ACCESS_ERROR)
        }
    }

    fun checkAccountUser(id: Long) {
        val account = accountRepository.findById(id)
            .orElseThrow { BookandException(ErrorCode.NOT_FOUND_MEMBER) }
        if (account.role.name != "USER") {
            throw BookandException(ErrorCode.ROLE_ACCESS_ERROR)
        }
    }

    fun checkNicknameBoolean(nickname: String, currentNickname: String): Boolean {
        return if (currentNickname == nickname) true
        else accountRepository.existsByNickname(nickname)
    }

    fun checkLoginMember(account: Account, authRequest: AuthRequest) {
        val socialIdWithAccessToken = authService.getSocialIdWithAccessToken(authRequest)
        val socialEmail =
            socialIdWithAccessToken.userId + "@" + account.provider.lowercase(Locale.getDefault()) + ".com"
        if (account.email != socialEmail) {
            throw BookandException(ErrorCode.NOT_MATCH_MEMBER)
        }
    }


}