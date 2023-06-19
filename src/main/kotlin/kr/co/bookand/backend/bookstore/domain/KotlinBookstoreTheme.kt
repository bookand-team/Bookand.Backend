package kr.co.bookand.backend.bookstore.domain

import javax.persistence.*

@Entity
class KotlinBookstoreTheme(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kbookstore_theme_id")
    var id: Long = 0,

    @Enumerated(EnumType.STRING)
    var theme: KotlinBookstoreType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kbookstore_id")
    var bookstore: KotlinBookstore? = null
) {
    fun updateBookStore(bookstore: KotlinBookstore) {
        this.bookstore = bookstore
    }
}