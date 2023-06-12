package kr.co.bookand.backend.dashboard.domain

import javax.persistence.*

@Entity
class KotlinDashBoard(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kdashboard_id")
    var id: Long = 0,

    var totalArticleNum: Long,
    var bookArticleNum: Long,
    var bookStoreArticleNum: Long,
    var interviewArticleNum: Long,
    var registrationBookStoreNum: Long,
    var shownBookStoreNum: Long,
    var totalBookMarkNum: Long,
    var articleBookMarkNum: Long,
    var articleBookMarkAvg: Long,
    var bookStoreBookMarkNum: Long,
    var bookStoreBookMarkAvg: Long,
    var totalAccountNum: Long,
    var totalRevokeNum: Long,
    var todayBookStoreNum: Long,
    var invisibleBookStoreNum: Long,
    var totalFeedBackNum: Long,
    var todayFeedBackNum: Long,
    var lastFeedBackNum: Long,
) {
    constructor(
        totalArticleNum: Long,
        bookArticleNum: Long,
        bookStoreArticleNum: Long,
        interviewArticleNum: Long,
        registrationBookStoreNum: Long,
        shownBookStoreNum: Long,
        totalBookMarkNum: Long,
        articleBookMarkNum: Long,
        articleBookMarkAvg: Long,
        bookStoreBookMarkNum: Long,
        bookStoreBookMarkAvg: Long,
        totalAccountNum: Long,
        totalRevokeNum: Long,
        todayBookStoreNum: Long,
        invisibleBookStoreNum: Long,
        totalFeedBackNum: Long,
        todayFeedBackNum: Long,
        lastFeedBackNum: Long
    ): this(
        id = 0,
        totalArticleNum = totalArticleNum,
        bookArticleNum = bookArticleNum,
        bookStoreArticleNum = bookStoreArticleNum,
        interviewArticleNum = interviewArticleNum,
        registrationBookStoreNum = registrationBookStoreNum,
        shownBookStoreNum = shownBookStoreNum,
        totalBookMarkNum = totalBookMarkNum,
        articleBookMarkNum = articleBookMarkNum,
        articleBookMarkAvg = articleBookMarkAvg,
        bookStoreBookMarkNum = bookStoreBookMarkNum,
        bookStoreBookMarkAvg = bookStoreBookMarkAvg,
        totalAccountNum = totalAccountNum,
        totalRevokeNum = totalRevokeNum,
        todayBookStoreNum = todayBookStoreNum,
        invisibleBookStoreNum = invisibleBookStoreNum,
        totalFeedBackNum = totalFeedBackNum,
        todayFeedBackNum = todayFeedBackNum,
        lastFeedBackNum = lastFeedBackNum
    )
}