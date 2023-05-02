package kr.co.bookand.backend.policy.repository

import kr.co.bookand.backend.policy.domain.KotlinPolicy
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinPolicyRepository : JpaRepository<KotlinPolicy, Long> {
    fun findByName(name: String): KotlinPolicy?
}