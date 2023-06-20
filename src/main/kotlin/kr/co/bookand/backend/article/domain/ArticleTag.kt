package kr.co.bookand.backend.article.domain

import javax.persistence.*

@Entity
class ArticleTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_tag_id")
    var id: Long = 0,
    var tag: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    var article: Article? = null
) {
    fun updateArticle(article: Article) {
        this.article = article
    }
}