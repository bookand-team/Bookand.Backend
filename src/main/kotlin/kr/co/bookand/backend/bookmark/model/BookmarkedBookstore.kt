package kr.co.bookand.backend.bookmark.model

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.bookstore.model.Bookstore
import kr.co.bookand.backend.common.model.BaseEntity
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