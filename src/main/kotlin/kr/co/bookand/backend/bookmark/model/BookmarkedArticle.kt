package kr.co.bookand.backend.bookmark.model

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.article.model.Article
import kr.co.bookand.backend.common.model.BaseEntity
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