package kr.co.bookand.backend.article.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.article.domain.dto.ArticleDto;
import kr.co.bookand.backend.article.service.ArticleService;
import kr.co.bookand.backend.common.ApiResponse;
import kr.co.bookand.backend.common.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static kr.co.bookand.backend.article.domain.dto.ArticleDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/article")
@Api(tags = "아티클 API")
public class ArticleController {

    private final ArticleService articleService;

    @ApiOperation(value = "아티클 단일 조회")
    @GetMapping("/{name}")
    public ApiResponse<ArticleResponse> getArticle(@PathVariable String name) {
        ArticleResponse article = articleService.getArticle(name);
        return ApiResponse.success(article);
    }

    @ApiOperation(value = "아티클 전체 조회")
    @GetMapping("")
    public ApiResponse<Page<ArticleResponse>> getArticleList(@PageableDefault(size = 10) Pageable pageable) {
        Page<ArticleResponse> articleList = articleService.getArticleList(pageable);
        return ApiResponse.success(articleList);
    }

    @ApiOperation(value = "아티클 생성")
    @PostMapping("")
    public ApiResponse<ArticleResponse> createArticle(@RequestBody ArticleRequest articleDto) {
        ArticleResponse article = articleService.createArticle(articleDto);
        return ApiResponse.success(article);
    }

    @ApiOperation(value = "아티클 수정")
    @PutMapping("")
    public ApiResponse<ArticleResponse> updateArticle(@RequestBody ArticleRequest articleDto) {
        ArticleResponse article = articleService.updateArticle(articleDto);
        return ApiResponse.success(article);
    }

    @ApiOperation(value = "아티클 삭제")
    @DeleteMapping("/{name}")
    public Message removeArticle(@PathVariable String name) {
        articleService.removeBookStore(name);
        return Message.of("아티클 삭제 완료.");
    }
}
