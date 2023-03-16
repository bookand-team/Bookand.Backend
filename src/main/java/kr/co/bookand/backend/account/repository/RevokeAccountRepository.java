package kr.co.bookand.backend.account.repository;

import kr.co.bookand.backend.account.domain.RevokeAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokeAccountRepository extends JpaRepository<RevokeAccount, Long> {
}
