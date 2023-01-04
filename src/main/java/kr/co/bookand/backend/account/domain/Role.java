package kr.co.bookand.backend.account.domain;

public enum Role {
    USER, ADMIN, MANAGER, BAN;

    public void checkAdmin() {
        if (this != ADMIN) {
            throw new IllegalStateException("관리자만 접근 가능합니다.");
        }
    }
}
