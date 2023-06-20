package kr.co.bookand.backend.dashboard.dto

data class StatusBoardResponse(
    val totalArticleNum: Long,
    val bookArticleNum: Long,
    val bookStoreArticleNum: Long,
    val interviewArticleNum: Long,
    val registrationBookStoreNum: Long,
    val shownBookStoreNum: Long,
    val totalBookMarkNum: Long,
    val articleBookMarkNum: Long,
    val articleBookMarkAvg: Long,
    val bookStoreBookMarkNum: Long,
    val bookStoreBookMarkAvg: Long,
    val totalAccountNum: Long,
    val totalRevokeNum: Long,
    val todayBookStoreNum: Long,
    val invisibleBookStoreNum: Long,
    val totalFeedBackNum: Long,
    val todayFeedBackNum: Long,
    val lastFeedBackNum: Long
)
