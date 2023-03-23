package kr.co.bookand.backend.map.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/maps")
@RequiredArgsConstructor
@Api(tags = "지도 API")
public class MapController {

    private final MapService mapService;

    @ApiOperation(value = "키워드로 검색")
    @GetMapping("/search")
    public String searchByKeyword(@RequestParam("query") String query, Pageable pageable) {
        return mapService.searchByKeyword(query, pageable);
    }
}
