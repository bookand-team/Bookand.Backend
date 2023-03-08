package kr.co.bookand.backend.feedback.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FeedbackType {
    로그인("로그인 오류"),
    PUSH("잦은 알림 불편"),
    업데이트("잦은 업데이트 불편"),
    정보오류("소개글 정보 부정확"),
    콘텐츠("소개글 콘텐츠 내용 미흡"),
    서점정보("지도 서점정보 부정확"),
    미등록서점("미등록 서점 존재"),
    UXUI("서비스 이용 방법이 불편함"),
    에러("시스템 장애/오류"),
    미분류("기타");

    private final String detail;

    public static FeedbackType getFeedbackType(String detail) {
        return switch (detail) {
            case "로그인 오류" -> FeedbackType.로그인;
            case "잦은 알림 불편" -> FeedbackType.PUSH;
            case "잦은 업데이트 불편" -> FeedbackType.업데이트;
            case "소개글 정보 부정확" -> FeedbackType.정보오류;
            case "소개글 콘텐츠 내용 미흡" -> FeedbackType.콘텐츠;
            case "지도 서점정보 부정확" -> FeedbackType.서점정보;
            case "미등록 서점 존재" -> FeedbackType.미등록서점;
            case "서비스 이용 방법이 불편함" -> FeedbackType.UXUI;
            case "시스템 장애/오류" -> FeedbackType.에러;
            case "기타" -> FeedbackType.미분류;
            default -> FeedbackType.미분류;
        };
    }
}

