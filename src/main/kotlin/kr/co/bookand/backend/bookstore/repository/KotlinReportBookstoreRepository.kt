package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.bookstore.domain.KotlinReportBookstore
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinReportBookstoreRepository : JpaRepository<KotlinReportBookstore, Long> {
}