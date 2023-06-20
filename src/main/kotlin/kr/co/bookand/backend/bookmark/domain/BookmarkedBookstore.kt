package kr.co.bookand.backend.bookmark.domain

import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.bookstore.domain.Bookstore
import kr.co.bookand.backend.common.domain.BaseEntity
import javax.persistence.*

@Entity
class BookmarkedBookstore(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmarked_bookstore_id")
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    var bookmark: Bookmark,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookstore_id")
    var bookstore: Bookstore,

    @ManyToOne(fetch = FetchType.LAZY)
    var account: Account? = null,
) : BaseEntity() {
}