package kr.co.bookand.backend.policy.repository;

import kr.co.bookand.backend.policy.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Optional<Policy> findByTitle(String title);
}
