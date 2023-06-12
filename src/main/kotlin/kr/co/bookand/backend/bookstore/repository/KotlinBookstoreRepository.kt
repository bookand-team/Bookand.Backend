package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import kr.co.bookand.backend.common.domain.Status
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinBookstoreRepository: JpaRepository<KotlinBookstore, Long> {

    fun existsByName(name: String): Boolean
    fun countAllByVisibility(visibility: Boolean): Long
    fun countAllByVisibilityAndStatus(visibility: Boolean, status: Status): Long
}