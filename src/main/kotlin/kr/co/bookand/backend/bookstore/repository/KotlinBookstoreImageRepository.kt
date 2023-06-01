package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.bookstore.domain.KotlinBookstoreImage
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinBookstoreImageRepository : JpaRepository<KotlinBookstoreImage, Long> {
    fun findByUrl(url: String): KotlinBookstoreImage
}