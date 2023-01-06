package kr.co.bookand.backend.dashboard.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DashBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalArticleNum;
    private Long bookArticleNum;
    private Long bookStoreArticleNum;
    private Long interviewArticleNum;

    private Long registrationBookStoreNum;
    private Long shownBookStoreNum;

    private Long totalBookMarkNum;
    private Long ArticleBookMarkNum;
    private Long ArticleBookMarkAvg;
    private Long bookStoreBookMarkNum;
    private Long bookStoreBookMarkAvg;

    private Long totalAccountNum;
    private Long AndroidAccountNum;
    private Long IosAccountNum;

    private Long totalRevokeNum;
    private Long AndroidRevokeNum;
    private Long IosRevokeNum;

    private Long todayBookStoreNum;
    private Long invisibleBookStoreNum;
    private Long totalFeedBackNum;
    private Long todayFeedBackNum;
    private Long lastFeedBackNum;


    public static DashBoard updateDashBoard(Long totalArticleNum, Long bookArticleNum, Long bookStoreArticleNum, Long interviewArticleNum,
                                            Long registrationBookStoreNum, Long shownBookStoreNum,
                                            Long totalBookMarkNum, Long ArticleBookMarkNum, Long ArticleBookMarkAvg, Long bookStoreBookMarkNum, Long bookStoreBookMarkAvg,
                                            Long totalAccountNum, Long AndroidAccountNum, Long IosAccountNum,
                                            Long totalRevokeNum, Long AndroidRevokeNum, Long IosRevokeNum,
                                            Long todayBookStoreNum, Long invisibleBookStoreNum, Long totalFeedBackNum, Long todayFeedBackNum, Long lastFeedBackNum) {
        return DashBoard.builder()
                .totalArticleNum(totalArticleNum)
                .bookArticleNum(bookArticleNum)
                .bookStoreArticleNum(bookStoreArticleNum)
                .interviewArticleNum(interviewArticleNum)
                .registrationBookStoreNum(registrationBookStoreNum)
                .shownBookStoreNum(shownBookStoreNum)
                .totalBookMarkNum(totalBookMarkNum)
                .ArticleBookMarkNum(ArticleBookMarkNum)
                .ArticleBookMarkAvg(ArticleBookMarkAvg)
                .bookStoreBookMarkNum(bookStoreBookMarkNum)
                .bookStoreBookMarkAvg(bookStoreBookMarkAvg)
                .totalAccountNum(totalAccountNum)
                .AndroidAccountNum(AndroidAccountNum)
                .IosAccountNum(IosAccountNum)
                .totalRevokeNum(totalRevokeNum)
                .AndroidRevokeNum(AndroidRevokeNum)
                .IosRevokeNum(IosRevokeNum)
                .todayBookStoreNum(todayBookStoreNum)
                .invisibleBookStoreNum(invisibleBookStoreNum)
                .totalFeedBackNum(totalFeedBackNum)
                .todayFeedBackNum(todayFeedBackNum)
                .lastFeedBackNum(lastFeedBackNum)
                .build();
    }
}
