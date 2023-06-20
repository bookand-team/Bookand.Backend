package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.bookstore.domain.BookstoreTheme
import org.springframework.data.jpa.repository.JpaRepository

interface BookstoreThemeRepository : JpaRepository<BookstoreTheme, Long> {
}