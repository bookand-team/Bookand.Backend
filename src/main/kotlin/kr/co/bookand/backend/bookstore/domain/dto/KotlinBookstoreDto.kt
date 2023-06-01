package kr.co.bookand.backend.bookstore.domain.dto

import kr.co.bookand.backend.common.domain.Status

data class KotlinBookstoreRequest(
    val name: String,
    val address: String,
    val businessHours: String,
    val contact: String,
    val facility: String,
    val sns: String,
    val latitude: String,
    val longitude: String,
    val introduction: String,
    val mainImage: String,
    val themeList: List<String>,
    val subImageList : List<String>
)

data class KotlinBookstoreListRequest(
    val bookstoreList: List<Long>
)

data class KotlinReportBookstoreRequest(
    val title: String,
    val content: String,
    val isAnswered: Boolean,
    val answerTitle: String,
    val answerContent: String,
    val answeredAt: String
)

data class KotlinAnswerReportRequest(
    val isAnswered: Boolean,
    val answerTitle: String,
    val answerContent: String
)

data class KotlinBookstoreResponse(
    val id: Long,
    val name: String,
    val address: String,
    val latitude: String,
    val longitude: String
)

data class KotlinBookstoreWebResponse(
    val id: Long,
    val name: String,
    val address: String,
    val businessHours: String,
    val contact: String,
    val facility: String,
    val sns: String,
    val latitude: String,
    val longitude: String,
    val introduction: String,
    val mainImage: String,
    val status: Status,
    val themeList: List<String>,
    val subImageList : List<String>
)