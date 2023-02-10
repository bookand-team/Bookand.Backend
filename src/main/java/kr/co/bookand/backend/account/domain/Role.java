package kr.co.bookand.backend.account.domain;

import kr.co.bookand.backend.account.exception.AccountException;
import kr.co.bookand.backend.common.exception.ErrorCode;

import javax.security.auth.message.AuthException;

public enum Role {
    USER, ADMIN, MANAGER, BAN;

    public void checkAdmin() {
        if (this != ADMIN) {
            throw new AccountException(ErrorCode.ROLE_ACCESS_ERROR, this);
        }
    }
}
