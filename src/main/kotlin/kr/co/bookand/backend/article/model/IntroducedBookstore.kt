package kr.co.bookand.backend.article.model

import kr.co.bookand.backend.article.dto.IntroducedBookstoreRequest
import kr.co.bookand.backend.bookstore.model.Bookstore
import javax.persistence.*

@Entity
class IntroducedBookstore(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "introduced_bookstore_id")
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    val article: Article,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookstore_id")
    val bookstore: Bookstore
) {
    constructor(introducedBookstoreRequest: IntroducedBookstoreRequest) : this(
        article = introducedBookstoreRequest.article,
        bookstore = introducedBookstoreRequest.bookstore
    )
}