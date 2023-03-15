package kr.co.bookand.backend.dashboard.repository;

import kr.co.bookand.backend.dashboard.domain.DashBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashBoardRepository extends JpaRepository<DashBoard, Long> {
}
