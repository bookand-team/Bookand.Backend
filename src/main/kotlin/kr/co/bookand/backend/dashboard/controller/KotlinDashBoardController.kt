package kr.co.bookand.backend.dashboard.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.co.bookand.backend.dashboard.domain.dto.StatusBoardResponse
import kr.co.bookand.backend.dashboard.service.KotlinDashBoardService
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/dashboard")
@RequiredArgsConstructor
@Api(tags = ["대시보드 API"])
class KotlinDashBoardController(
    private val kotlinDashBoardService: KotlinDashBoardService
) {

    @ApiOperation(value = "현황 통계")
    @GetMapping("/statusBoard")
    fun getStatusBoard(): ResponseEntity<StatusBoardResponse> {
        return ResponseEntity.ok(kotlinDashBoardService.getStatusBoard())
    }

    @Scheduled(cron = "0 0 0 * * *")
    fun updateStatusBoard() {
        kotlinDashBoardService.updateStatusBoard()
    }
}