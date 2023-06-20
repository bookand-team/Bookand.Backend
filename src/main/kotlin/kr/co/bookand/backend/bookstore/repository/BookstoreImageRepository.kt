package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.bookstore.domain.BookstoreImage
import org.springframework.data.jpa.repository.JpaRepository

interface BookstoreImageRepository : JpaRepository<BookstoreImage, Long> {
    fun findByUrl(url: String): BookstoreImage
}