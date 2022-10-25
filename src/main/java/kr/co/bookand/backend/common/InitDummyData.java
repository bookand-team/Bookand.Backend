package kr.co.bookand.backend.common;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.SocialType;
import kr.co.bookand.backend.account.domain.dto.AuthDto;
import kr.co.bookand.backend.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitDummyData {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private String suffix;

    @PostConstruct
    public void dummyData() {
        AuthDto.MiddleAccount middleAccount = AuthDto.MiddleAccount.builder()
                .email("email")
                .socialType(SocialType.GOOGLE)
                .providerEmail("providerEmail")
                .build();
        Account account = middleAccount.toAdmin(passwordEncoder, suffix);
        accountRepository.save(account);
    }
}
