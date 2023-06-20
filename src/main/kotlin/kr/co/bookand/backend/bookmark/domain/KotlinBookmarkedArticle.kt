package kr.co.bookand.backend.bookmark.domain

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.common.domain.KotlinBaseEntity
import javax.persistence.*

@Entity
class KotlinBookmarkedArticle(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kbookmarked_article_id")
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kbookmark_id")
    var bookmark: KotlinBookmark,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "karticle_id")
    var article: KotlinArticle,

    @ManyToOne(fetch = FetchType.LAZY)
    var account: KotlinAccount? = null,
) : KotlinBaseEntity() {
}