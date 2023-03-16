package kr.co.bookand.backend.account.domain;

import kr.co.bookand.backend.account.exception.AccountException;
import kr.co.bookand.backend.common.exception.ErrorCode;

public enum Role {
    USER, ADMIN, MANAGER, BAN;

    public void checkAdmin() {
        if (this != ADMIN) {
            throw new AccountException(ErrorCode.ROLE_ACCESS_ERROR, this);
        }
    }

    public void checkNotUser() {
        if (this != MANAGER && this != ADMIN) {
            throw new AccountException(ErrorCode.ROLE_ACCESS_ERROR, this);
        }
    }
}
