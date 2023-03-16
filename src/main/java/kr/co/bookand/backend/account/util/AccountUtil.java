package kr.co.bookand.backend.account.util;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.config.security.PrincipalDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

public class AccountUtil {
    private AccountUtil() {
    }

    public static Account getAccount() {
        Authentication authentication = getAuthentication();
        PrincipalDetails principalDetails = getPrincipalDetails(authentication);
        return getAccountFromPrincipalDetails(principalDetails);
    }

    private static Authentication getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authentication object is null"));
    }

    private static PrincipalDetails getPrincipalDetails(Authentication authentication) {
        Object principal = Optional.ofNullable(authentication.getPrincipal())
                .filter(p -> p instanceof PrincipalDetails)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Principal is not an instance of PrincipalDetails"));

        return (PrincipalDetails) principal;
    }

    private static Account getAccountFromPrincipalDetails(PrincipalDetails principalDetails) {
        return Optional.ofNullable(principalDetails.getAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is null"));
    }
}
