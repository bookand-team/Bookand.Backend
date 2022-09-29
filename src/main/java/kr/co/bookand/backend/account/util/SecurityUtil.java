package kr.co.bookand.backend.account.util;

import kr.co.bookand.backend.account.exception.NotFoundUserInformationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {

    }

    public static String getCurrentAccountEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() ==null){
            throw new NotFoundUserInformationException();
        }
        return authentication.getName();
    }
}
