package kr.co.bookand.backend.account.repository;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.SuspendedAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuspendedAccountRepository extends JpaRepository<SuspendedAccount, Long> {
    Optional<SuspendedAccount> findByAccount(Account account);
}
