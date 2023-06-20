package kr.co.bookand.backend.bookmark.domain

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import kr.co.bookand.backend.common.domain.KotlinBaseEntity
import javax.persistence.*

@Entity
class KotlinBookmarkedBookstore(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kbookmarked_bookstore_id")
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kbookmark_id")
    var bookmark: KotlinBookmark,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kbookstore_id")
    var bookstore: KotlinBookstore,

    @ManyToOne(fetch = FetchType.LAZY)
    var account: KotlinAccount? = null,
) : KotlinBaseEntity() {
}