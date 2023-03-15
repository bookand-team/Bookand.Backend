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
}

