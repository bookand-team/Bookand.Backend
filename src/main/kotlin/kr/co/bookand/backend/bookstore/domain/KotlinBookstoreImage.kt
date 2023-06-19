package kr.co.bookand.backend.bookstore.domain

import javax.persistence.*

@Entity
class KotlinBookstoreImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kbookstore_image_id")
    var id: Long = 0,

    var url: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kbookstore_id")
    var bookstore: KotlinBookstore? = null
) {
    fun updateBookStore(bookstore: KotlinBookstore) {
        this.bookstore = bookstore
    }
}