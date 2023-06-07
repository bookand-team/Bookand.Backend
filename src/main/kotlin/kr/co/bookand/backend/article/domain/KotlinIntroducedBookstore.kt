package kr.co.bookand.backend.article.domain

import kr.co.bookand.backend.article.domain.dto.KotlinIntroducedBookstoreRequest
import kr.co.bookand.backend.bookstore.domain.BookStore
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import javax.persistence.*
import kotlin.jvm.internal.Intrinsics.Kotlin

@Entity
class KotlinIntroducedBookstore(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kintroduced_bookstore_id")
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "karticle_id")
    val article: KotlinArticle,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kbookstore_id")
    val bookStore: KotlinBookstore
) {
    constructor(introducedBookstoreRequest: KotlinIntroducedBookstoreRequest) : this(
        article = introducedBookstoreRequest.article,
        bookStore = introducedBookstoreRequest.bookstore
    )
}