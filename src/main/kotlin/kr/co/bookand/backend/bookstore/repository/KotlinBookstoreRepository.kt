package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinBookstoreRepository: JpaRepository<KotlinBookstore, Long> {

    fun existsByName(name: String): Boolean
}