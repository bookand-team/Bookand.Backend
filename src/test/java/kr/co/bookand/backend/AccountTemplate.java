package kr.co.bookand.backend;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AccountTemplate {
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private static Long id = 10000L;

    public static Account makeAccount1() {
        Account account = Account.builder()
                .email("email")
                .password(bCryptPasswordEncoder.encode("password"))
                .nickname("nickname")
                .role(Role.ADMIN)
                .build();
        account.setIdForTest(id++);
        return account;
    }
}
