package kr.co.bookand.backend.feedback.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FeedbackType {
    LOGIN("로그인"),
    PUSH("PUSH"),
    UPDATE("업데이트"),
    INFORMATION_ERROR("정보 오류"),
    CONTENTS("콘텐츠"),
    BOOKSTORE_INFO("서점정보"),
    UNKNOWN_BOOKSTORE("미등록서점"),
    UXUI("UXUI"),
    ERROR("에러"),
    ETC("미분류");

    private final String detail;

    public String toDetail() {
        return detail;
    }

    public static FeedbackType getFeedbackType(String detail) {
        return switch (detail) {
            case "로그인" -> FeedbackType.LOGIN;
            case "PUSH" -> FeedbackType.PUSH;
            case "업데이트" -> FeedbackType.UPDATE;
            case "정보 오류" -> FeedbackType.INFORMATION_ERROR;
            case "콘텐츠" -> FeedbackType.CONTENTS;
            case "서점정보" -> FeedbackType.BOOKSTORE_INFO;
            case "미등록서점" -> FeedbackType.UNKNOWN_BOOKSTORE;
            case "UXUI" -> FeedbackType.UXUI;
            case "에러" -> FeedbackType.ERROR;
            case "미분류" -> FeedbackType.ETC;
            default -> FeedbackType.ETC;
        };
    }
}

