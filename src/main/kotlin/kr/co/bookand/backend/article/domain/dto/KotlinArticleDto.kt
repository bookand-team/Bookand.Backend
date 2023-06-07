package kr.co.bookand.backend.article.domain.dto

import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore

data class KotlinArticleRequest(
    val title: String,
    val subTitle: String,
    val mainImage: String,
    val content: String,
    val category: String,
    val writer: String,
    val tagList: List<String>,
    val bookStoreList: List<Long>,
)
data class KotlinIntroducedBookstoreRequest(
    val article: KotlinArticle,
    val bookstore: KotlinBookstore
)

data class KotlinArticleResponse(
    val id: Long,
    val title: String,
    val subTitle: String,
    val mainImage: String,
    val content: String,
    val category: String,
    val writer: String,
    val tagList: List<String>,
    val bookStoreList: List<Long>,
)

fun KotlinArticle.toResponse(): KotlinArticleResponse {
    return KotlinArticleResponse(
        id = id,
        title = title,
        subTitle = subTitle,
        mainImage = mainImage,
        content = content,
        category = category.toString(),
        writer = writer,
        tagList = articleTagList.map { it.tag },
        bookStoreList = introducedBookstoreList.map { it.bookStore.id }
    )
}