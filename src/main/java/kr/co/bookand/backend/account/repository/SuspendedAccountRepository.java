package kr.co.bookand.backend.account.repository;

import kr.co.bookand.backend.account.domain.SuspendedAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuspendedAccountRepository extends JpaRepository<SuspendedAccount, Long> {
}
