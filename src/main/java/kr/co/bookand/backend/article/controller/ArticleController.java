package kr.co.bookand.backend.article.controller;

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
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{name}")
    public ApiResponse<ArticleResponse> getArticle(@PathVariable String name) {
        ArticleResponse article = articleService.getArticle(name);
        return ApiResponse.success(article);
    }

    @GetMapping("")
    public ApiResponse<Page<ArticleResponse>> getArticleList(@PageableDefault(size = 10) Pageable pageable) {
        Page<ArticleResponse> articleList = articleService.getArticleList(pageable);
        return ApiResponse.success(articleList);
    }

    @PostMapping("")
    public ApiResponse<ArticleResponse> createArticle(@RequestBody ArticleRequest articleDto) {
        ArticleResponse article = articleService.createArticle(articleDto);
        return ApiResponse.success(article);
    }

    @PutMapping("")
    public ApiResponse<ArticleResponse> updateArticle(@RequestBody ArticleRequest articleDto) {
        ArticleResponse article = articleService.updateArticle(articleDto);
        return ApiResponse.success(article);
    }

    @DeleteMapping("/{name}")
    public Message removeArticle(@PathVariable String name) {
        articleService.removeBookStore(name);
        return Message.of("아티클 삭제 완료.");
    }
}
