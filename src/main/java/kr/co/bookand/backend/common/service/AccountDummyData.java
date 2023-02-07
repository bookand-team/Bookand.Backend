package kr.co.bookand.backend.common.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.SocialType;
import kr.co.bookand.backend.account.domain.dto.AuthDto;
import kr.co.bookand.backend.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class AccountDummyData {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${admin.secret}")
    private CharSequence ADMIN_PASSWORD;

    @Transactional
    @PostConstruct
    public void dummyData() {
        AuthDto.MiddleAccount middleAccount = AuthDto.MiddleAccount.builder()
                .email("email")
                .socialType(SocialType.GOOGLE)
                .providerEmail("providerEmail")
                .build();
        Account account = middleAccount.toAdmin(passwordEncoder, ADMIN_PASSWORD);
        accountRepository.save(account);
    }
}
