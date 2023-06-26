package kr.co.bookand.backend.bookstore.repository

import kr.co.bookand.backend.bookstore.model.BookstoreTheme
import org.springframework.data.jpa.repository.JpaRepository

interface BookstoreThemeRepository : JpaRepository<BookstoreTheme, Long> {
}