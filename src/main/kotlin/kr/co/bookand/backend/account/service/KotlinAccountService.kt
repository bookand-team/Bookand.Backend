package kr.co.bookand.backend.account.service

import kr.co.bookand.backend.account.domain.*
import kr.co.bookand.backend.account.domain.dto.*
import kr.co.bookand.backend.account.repository.KotlinAccountRepository
import kr.co.bookand.backend.account.repository.KotlinRevokeAccountRepository
import kr.co.bookand.backend.account.repository.KotlinSuspendedAccountRepository
import kr.co.bookand.backend.common.KotlinErrorCode
import kr.co.bookand.backend.common.KotlinPageResponse
import kr.co.bookand.backend.common.domain.KotlinMessageResponse
import kr.co.bookand.backend.config.jwt.KotlinRefreshTokenRepository
import kr.co.bookand.backend.config.security.KotlinSecurityUtils.getCurrentAccountEmail
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*


@Service
@Transactional(readOnly = true)
class KotlinAccountService(
    private val kotlinAccountRepository: KotlinAccountRepository,
    private val kotlinSuspendedAccountRepository: KotlinSuspendedAccountRepository,
    private val kotlinRevokeAccountRepository: KotlinRevokeAccountRepository,
    private val kotlinRefreshTokenRepository: KotlinRefreshTokenRepository,
    private val kotlinAuthService: KotlinAuthService
) {

    fun getCurrentAccount(): KotlinAccount {
        return kotlinAccountRepository.findByEmail(getCurrentAccountEmail())
            ?: throw IllegalArgumentException("존재하지 않는 계정입니다.")
    }

    fun getMyAccountInfo(): KotlinAccountInfoResponse {
        return KotlinAccountInfoResponse(getCurrentAccount())
    }

    fun getAccountByEmail(email: String): KotlinAccount {
        return kotlinAccountRepository.findByEmail(email)
            ?: throw IllegalArgumentException("존재하지 않는 계정입니다.")
    }

    fun getAccountByNickname(nickname: String): KotlinAccountInfoResponse {
        val account = kotlinAccountRepository.findByNickname(nickname)
            ?: throw IllegalArgumentException("존재하지 않는 계정입니다.")
        return KotlinAccountInfoResponse(account)
    }

    fun getAccountById(id: Long): KotlinAccount {
        return kotlinAccountRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 계정입니다.") }
    }

    fun getAccountInfoById(id: Long): KotlinAccountInfoResponse {
        return KotlinAccountInfoResponse(getAccountById(id))
    }

    fun getAccountList(pageable: Pageable): KotlinAccountListResponse {
        val accountList = kotlinAccountRepository.findAll(pageable)
            .map { KotlinAccountInfoResponse(it) }
        return KotlinAccountListResponse(KotlinPageResponse.of(accountList))
    }

    fun getRandomNickname(): KotlinNicknameResponse {
        var nicknameRandom = kotlinAuthService.nicknameRandom()
        while (checkNicknameBoolean(nicknameRandom, "")) {
            nicknameRandom = kotlinAuthService.nicknameRandom()
        }
        return KotlinNicknameResponse(nicknameRandom)
    }

    @Transactional
    fun updateAccount(currentAccount: KotlinAccount, request: KotlinAccountRequest): KotlinAccountInfoResponse {
        val checkNicknameBoolean = checkNicknameBoolean(request.nickname, currentAccount.nickname)
        if (checkNicknameBoolean) {
            throw RuntimeException(KotlinErrorCode.NICKNAME_DUPLICATION.errorMessage)
        }
        currentAccount.updateProfile(request.profileImage, request.nickname)
        return KotlinAccountInfoResponse(currentAccount)
    }

    fun existNickname(nickname: String): KotlinMessageResponse {
        val existsByNickname = kotlinAccountRepository.existsByNickname(nickname)
        return if (existsByNickname) {
            KotlinMessageResponse("CONFLICT", 409)
        } else {
            KotlinMessageResponse("OK", 200)
        }
    }

    @Transactional
    fun revokeAccount(currentAccount: KotlinAccount, request: KotlinRevokeReasonRequest): Boolean {
        val checkRevokeType = KotlinRevokeType.checkRevokeType(request.revokeType)
        val authRequest = KotlinAuthRequest(
            accessToken = request.socialAccessToken,
            socialType = currentAccount.provider,
        )
        checkLoginMember(currentAccount, authRequest)
        val revokeAccount = KotlinRevokeAccount(
            reason = request.reason,
            revokeType = checkRevokeType,
            accountId = currentAccount.id
        )
        kotlinRevokeAccountRepository.save(revokeAccount)
        kotlinRefreshTokenRepository.deleteByAccountId(currentAccount.id)
        kotlinAccountRepository.delete(currentAccount)
        return true
    }

    @Transactional
    fun suspendedAccount(currentAccount: KotlinAccount, accountId: Long): KotlinAccountStatus {
        checkAccountAdmin(currentAccount.id)
        val account = getAccountById(accountId)
        val suspendedAccount = getSuspendedAccount(account)
            ?: kotlinSuspendedAccountRepository.save(KotlinSuspendedAccount(account = account))
        return setSuspendedAccount(account, suspendedAccount)
    }

    fun setSuspendedAccount(account: KotlinAccount, suspendedAccount: KotlinSuspendedAccount): KotlinAccountStatus {
        val suspendedCount = suspendedAccount.suspendedCount

        val suspendedAt =
            if (suspendedCount == 0) LocalDateTime.now().plusDays(7)
            else LocalDateTime.now().plusMonths(6)

        val newStatus =
            if (suspendedCount == 0) KotlinAccountStatus.SUSPENDED
            else KotlinAccountStatus.DELETED

        suspendedAccount.setSuspendedAt(suspendedAt)
        suspendedAccount.addSuspendedCount()
        account.updateAccountStatus(newStatus)

        if (newStatus == KotlinAccountStatus.DELETED) account.deletedBanAccount()
        return newStatus
    }

    private fun getSuspendedAccount(account: KotlinAccount): KotlinSuspendedAccount? {
        return kotlinSuspendedAccountRepository.findByAccount(account)
    }


    fun isAdmin(email: String): Boolean {
        val account = kotlinAccountRepository.findByEmail(email) ?: return false
        return account.role.name == "ADMIN"
    }

    fun isUser(email: String): Boolean {
        val account = kotlinAccountRepository.findByEmail(email) ?: return false
        return account.role.name == "USER"
    }


    fun checkAccountAdmin(id: Long) {
        val account = kotlinAccountRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 계정입니다.") }
        if (account.role.name != "ADMIN") {
            throw IllegalArgumentException("관리자 계정이 아닙니다.")
        }
    }

    fun checkAccountUser(id: Long) {
        val account = kotlinAccountRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 계정입니다.") }
        if (account.role.name != "USER") {
            throw IllegalArgumentException("일반 계정이 아닙니다.")
        }
    }

    fun checkNicknameBoolean(nickname: String, currentNickname: String): Boolean {
        return if (currentNickname == nickname) true
        else kotlinAccountRepository.existsByNickname(nickname)
    }

    fun checkLoginMember(account: KotlinAccount, authRequest: KotlinAuthRequest) {
        val socialIdWithAccessToken = kotlinAuthService.getSocialIdWithAccessToken(authRequest)
        val socialEmail =
            socialIdWithAccessToken.userId + "@" + account.provider.lowercase(Locale.getDefault()) + ".com"
        if (account.email != socialEmail) {
            throw RuntimeException(KotlinErrorCode.NOT_MATCH_MEMBER.errorMessage)
        }
    }


}