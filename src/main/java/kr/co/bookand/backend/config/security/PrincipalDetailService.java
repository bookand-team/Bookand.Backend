package kr.co.bookand.backend.config.security;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.exception.AccountException;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findByEmailAndVisibilityTrue(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_MEMBER, email));
    }
    private PrincipalDetails createUserDetails(Account account){
        return new PrincipalDetails(account);
    }

}