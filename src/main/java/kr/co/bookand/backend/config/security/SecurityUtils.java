package kr.co.bookand.backend.config.security;

import kr.co.bookand.backend.account.exception.AccountException;
import kr.co.bookand.backend.common.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static String getCurrentAccountEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() ==null){
            throw new AccountException(ErrorCode.NOT_FOUND_MEMBER, null);
        }
        return authentication.getName();
    }
}
