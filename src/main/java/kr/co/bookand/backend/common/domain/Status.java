package kr.co.bookand.backend.common.domain;

public enum Status {
    INVISIBLE, VISIBLE, REMOVE;

    public static Status toEnum(String status) {
        if (status.equals("VISIBLE")) {
            return VISIBLE;
        } else if (status.equals("INVISIBLE")) {
            return INVISIBLE;
        } else if (status.equals("REMOVE")) {
            return REMOVE;
        } else {
            throw new IllegalArgumentException("잘못된 상태값입니다.");
        }
    }
}
