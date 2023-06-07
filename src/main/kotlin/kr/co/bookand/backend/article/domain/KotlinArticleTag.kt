package kr.co.bookand.backend.article.domain

import javax.persistence.*

@Entity
class KotlinArticleTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "karticle_tag_id")
    var id: Long = 0,
    var tag: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "karticle_id")
    var article: KotlinArticle? = null
) {
    fun updateArticle(article: KotlinArticle) {
        this.article = article
    }
}