package kr.co.bookand.backend.dashboard.repository

import kr.co.bookand.backend.dashboard.domain.KotlinDashBoard
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinDashBoardRepository : JpaRepository<KotlinDashBoard, Long> {
}