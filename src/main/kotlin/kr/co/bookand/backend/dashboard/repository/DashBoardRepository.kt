package kr.co.bookand.backend.dashboard.repository

import kr.co.bookand.backend.dashboard.model.DashBoard
import org.springframework.data.jpa.repository.JpaRepository

interface DashBoardRepository : JpaRepository<DashBoard, Long> {
}