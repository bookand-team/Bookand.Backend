package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.bookstore.domain.KotlinBookstoreTheme
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinBookstoreThemeRepository : JpaRepository<KotlinBookstoreTheme, Long> {
}