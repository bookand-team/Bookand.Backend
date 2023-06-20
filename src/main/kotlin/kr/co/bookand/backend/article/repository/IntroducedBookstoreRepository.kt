package kr.co.bookand.backend.article.repository

import kr.co.bookand.backend.article.domain.IntroducedBookstore
import org.springframework.data.jpa.repository.JpaRepository

interface IntroducedBookstoreRepository : JpaRepository<IntroducedBookstore, Long> {

}