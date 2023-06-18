package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.bookstore.domain.BookStore
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import kr.co.bookand.backend.common.domain.Status
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinBookstoreRepository: JpaRepository<KotlinBookstore, Long>, KotlinBookstoreRepositoryCustom {

    fun existsByName(name: String): Boolean
    fun countAllByVisibility(visibility: Boolean): Long
    fun countAllByVisibilityAndStatus(visibility: Boolean, status: Status): Long
    fun findAllByStatus(status: Status?, pageable: Pageable?): Page<KotlinBookstore>

    fun findAllByStatus(status: Status): List<KotlinBookstore>


}