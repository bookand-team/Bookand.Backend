package kr.co.bookand.backend.bookstore.model

import javax.persistence.*

@Entity
class BookstoreImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookstore_image_id")
    var id: Long = 0,

    var url: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookstore_id")
    var bookstore: Bookstore? = null
) {
    fun updateBookStore(bookstore: Bookstore) {
        this.bookstore = bookstore
    }
}