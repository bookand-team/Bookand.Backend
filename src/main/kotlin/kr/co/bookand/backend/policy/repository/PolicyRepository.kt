package kr.co.bookand.backend.policy.repository

import kr.co.bookand.backend.policy.domain.Policy
import org.springframework.data.jpa.repository.JpaRepository

interface PolicyRepository : JpaRepository<Policy, Long> {
    fun findByName(name: String): Policy?
}