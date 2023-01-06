package kr.co.bookand.backend.dashboard.domain.dto;

import kr.co.bookand.backend.dashboard.domain.DashBoard;
import lombok.Builder;

public class DashBoardDto {

    public record StatusBoardResponse(
            Long totalArticleNum,
            Long bookArticleNum,
            Long bookStoreArticleNum,
            Long interviewArticleNum,

            Long registrationBookStoreNum,
            Long shownBookStoreNum,

            Long totalBookMarkNum,
            Long ArticleBookMarkNum,
            Long ArticleBookMarkAvg,
            Long bookStoreBookMarkNum,
            Long bookStoreBookMarkAvg,

            Long totalAccountNum,
            Long AndroidAccountNum,
            Long IosAccountNum,

            Long totalRevokeNum,
            Long AndroidRevokeNum,
            Long IosRevokeNum,

            Long todayBookStoreNum,
            Long invisibleBookStoreNum,
            Long totalFeedBackNum,
            Long todayFeedBackNum,
            Long lastFeedBackNum) {

        @Builder
        public StatusBoardResponse {
        }

        public static StatusBoardResponse of(DashBoard dashBoard) {
            return StatusBoardResponse.builder()
                    .totalArticleNum(dashBoard.getTotalArticleNum())
                    .bookArticleNum(dashBoard.getBookArticleNum())
                    .bookStoreArticleNum(dashBoard.getBookStoreArticleNum())
                    .interviewArticleNum(dashBoard.getInterviewArticleNum())
                    .registrationBookStoreNum(dashBoard.getRegistrationBookStoreNum())
                    .shownBookStoreNum(dashBoard.getShownBookStoreNum())
                    .totalBookMarkNum(dashBoard.getTotalBookMarkNum())
                    .ArticleBookMarkNum(dashBoard.getArticleBookMarkNum())
                    .ArticleBookMarkAvg(dashBoard.getArticleBookMarkAvg())
                    .bookStoreBookMarkNum(dashBoard.getBookStoreBookMarkNum())
                    .bookStoreBookMarkAvg(dashBoard.getBookStoreBookMarkAvg())
                    .totalAccountNum(dashBoard.getTotalAccountNum())
                    .AndroidAccountNum(dashBoard.getAndroidAccountNum())
                    .IosAccountNum(dashBoard.getIosAccountNum())
                    .totalRevokeNum(dashBoard.getTotalRevokeNum())
                    .AndroidRevokeNum(dashBoard.getAndroidRevokeNum())
                    .IosRevokeNum(dashBoard.getIosRevokeNum())
                    .todayBookStoreNum(dashBoard.getTodayBookStoreNum())
                    .invisibleBookStoreNum(dashBoard.getInvisibleBookStoreNum())
                    .totalFeedBackNum(dashBoard.getTotalFeedBackNum())
                    .todayFeedBackNum(dashBoard.getTodayFeedBackNum())
                    .lastFeedBackNum(dashBoard.getLastFeedBackNum())
                    .build();
        }
    }
}
