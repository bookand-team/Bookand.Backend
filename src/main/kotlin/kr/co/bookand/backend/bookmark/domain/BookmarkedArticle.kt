package kr.co.bookand.backend.bookmark.domain

import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.article.domain.Article
import kr.co.bookand.backend.common.domain.BaseEntity
import javax.persistence.*

@Entity
class BookmarkedArticle(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmarked_article_id")
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    var bookmark: Bookmark,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    var article: Article,

    @ManyToOne(fetch = FetchType.LAZY)
    var account: Account? = null,
) : BaseEntity() {
}