package kr.co.bookand.backend.common

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {
    @GetMapping("/")
    fun hello(): String {
        return "BOOKAND REST API Server is running!"
    }

    @PostMapping("/")
    fun health(): String {
        return "BOOKAND REST API Server is running!"
    }
}