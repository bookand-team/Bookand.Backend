package kr.co.bookand.backend.account.util;

import kr.co.bookand.backend.account.exception.AccountException;
import kr.co.bookand.backend.common.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {

    }

    public static String getCurrentAccountEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AccountException(ErrorCode.NOT_FOUND_MEMBER, authentication);
        }
        return authentication.getName();
    }
}
