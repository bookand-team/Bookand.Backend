package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.bookstore.domain.Bookstore
import kr.co.bookand.backend.common.Status
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface BookstoreRepository: JpaRepository<Bookstore, Long>, BookstoreRepositoryCustom {

    fun existsByName(name: String): Boolean
    fun countAllByVisibility(visibility: Boolean): Long
    fun countAllByVisibilityAndStatus(visibility: Boolean, status: Status): Long
    fun findAllByStatus(status: Status?, pageable: Pageable): Page<Bookstore>

    fun findAllByStatus(status: Status): List<Bookstore>


}