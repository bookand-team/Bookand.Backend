package kr.co.bookand.backend.config.security;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.exception.AccountException;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class SecurityUtils {

    private final AccountRepository accountRepository;
    public static String getCurrentAccountEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() ==null){
            throw new AccountException(ErrorCode.NOT_FOUND_MEMBER, null);
        }
        return authentication.getName();
    }

    public static Account getCurrentAccount(AccountRepository accountRepository) {
        String currentAccountEmail = getCurrentAccountEmail();
        return accountRepository.findByEmailAndVisibilityTrue(currentAccountEmail).orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_MEMBER, null));
    }

}
