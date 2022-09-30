package kr.co.bookand.backend.account.util;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.config.security.PrincipalDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class AccountUtil {
    private AccountUtil(){
    }
    public static Account getAccount() {
        PrincipalDetails principal = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getAccount();
    }

}
