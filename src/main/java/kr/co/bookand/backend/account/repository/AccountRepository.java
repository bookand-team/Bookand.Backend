package kr.co.bookand.backend.account.repository;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.common.DeviceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    Optional<Account> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Long countBy();

    Long countByDeviceType(DeviceType userType);
}
