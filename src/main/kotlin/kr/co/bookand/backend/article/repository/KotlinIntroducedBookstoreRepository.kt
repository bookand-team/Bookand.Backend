package kr.co.bookand.backend.article.repository

import kr.co.bookand.backend.article.domain.KotlinIntroducedBookstore
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinIntroducedBookstoreRepository : JpaRepository<KotlinIntroducedBookstore, Long> {
}