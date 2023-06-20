package kr.co.bookand.backend.map.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.co.bookand.backend.map.service.KotlinMapService
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/maps")
@RequiredArgsConstructor
@Api(tags = ["지도 API"])
class KotlinMapController(
    private val mapService: KotlinMapService
) {
    @ApiOperation(value = "키워드로 검색")
    @GetMapping("/search")
    fun searchByKeyword(@RequestParam("query") query: String, pageable: Pageable): Any? {
        return mapService.searchByKeyword(query, pageable)
    }

}