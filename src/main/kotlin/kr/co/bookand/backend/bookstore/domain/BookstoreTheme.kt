package kr.co.bookand.backend.bookstore.domain

import javax.persistence.*

@Entity
class BookstoreTheme(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookstore_theme_id")
    var id: Long = 0,

    @Enumerated(EnumType.STRING)
    var theme: BookstoreType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookstore_id")
    var bookstore: Bookstore? = null
) {
    fun updateBookStore(bookstore: Bookstore) {
        this.bookstore = bookstore
    }
}