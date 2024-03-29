package kr.co.bookand.backend.dashboard.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.dashboard.dto.StatusBoardResponse
import kr.co.bookand.backend.dashboard.service.DashBoardService
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/dashboard")
@RequiredArgsConstructor
@Api(tags = ["대시보드 API"])
class DashBoardController(
    private val dashBoardService: DashBoardService,
    private val accountService: AccountService
) {

    @ApiOperation(value = "현황 통계")
    @GetMapping("/statusBoard")
    fun getStatusBoard(): ResponseEntity<StatusBoardResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(dashBoardService.getStatusBoard(account))
    }

    @Scheduled(cron = "0 0 0 * * *")
    fun updateStatusBoardScheduled() {
        dashBoardService.updateStatusBoard()
    }

    @PostMapping("/update")
    fun updateStatusBoard() {
        val account = accountService.getCurrentAccount()
        account.role.checkAdminAndManager()
        dashBoardService.updateStatusBoard()
    }
}