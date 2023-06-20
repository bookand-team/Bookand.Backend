package kr.co.bookand.backend.article.domain

import kr.co.bookand.backend.article.domain.dto.IntroducedBookstoreRequest
import kr.co.bookand.backend.bookstore.domain.Bookstore
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