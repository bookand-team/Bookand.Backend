package kr.co.bookand.backend.account.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RevokeType {

    NOT_ENOUGH_CONTENT("콘텐츠가 만족스럽지 않아요"),
    UNCOMFORTABLE("이용 방법이 불편해요"),
    PRIVACY("개인정보 보안이 걱정돼요"),
    ETC("기타");;
    private String reason;

    public static RevokeType of(String RevokeType) {
        return switch (RevokeType) {
            case "NOT_ENOUGH_CONTENT" -> NOT_ENOUGH_CONTENT;
            case "UNCOMFORTABLE" -> UNCOMFORTABLE;
            case "PRIVACY" -> PRIVACY;
            case "ETC" -> ETC;
            default -> throw new IllegalArgumentException("해당하는 타입이 없습니다.");
        };
    }
}
