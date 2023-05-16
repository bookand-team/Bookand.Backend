package kr.co.bookand.backend.account.service

import kr.co.bookand.backend.account.domain.dto.KotlinAccountInfoResponse
import kr.co.bookand.backend.account.domain.dto.KotlinAccountRequest
import kr.co.bookand.backend.account.repository.KotlinAccountRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class KotlinAccountService(
    private val kotlinAccountRepository: KotlinAccountRepository
) {
    @Transactional
    fun updateAccount(id: Long, request: KotlinAccountRequest): KotlinAccountInfoResponse {
        val account = kotlinAccountRepository.findById(id).orElseThrow { IllegalArgumentException("Account not found") }
        if (kotlinAccountRepository.existsByNickname(request.nickname)) {
            throw IllegalArgumentException("Nickname is already taken")
        }
        account.updateProfile(request.profileImage, request.nickname)
        return KotlinAccountInfoResponse(account)
    }


    fun isAdmin(email: String): Boolean {
        val account = kotlinAccountRepository.findByEmail(email) ?: return false
        return account.role.name == "ADMIN"
    }

    fun isUser(email: String): Boolean {
        val account = kotlinAccountRepository.findByEmail(email) ?: return false
        return account.role.name == "USER"
    }

    fun getAccount(id: Long?): KotlinAccountInfoResponse {
        return kotlinAccountRepository.findById(id)
            .map { KotlinAccountInfoResponse(it) }
            .orElseThrow { IllegalArgumentException("존재하지 않는 계정입니다.") }
    }

    fun getAccount(nickname: String): KotlinAccountInfoResponse {
        val account = kotlinAccountRepository.findByNickname(nickname)
            ?: throw IllegalArgumentException("존재하지 않는 계정입니다.")
        return KotlinAccountInfoResponse(account)
    }
}