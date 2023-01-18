package kr.co.bookand.backend.article.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.article.domain.dto.ArticleListDto;
import kr.co.bookand.backend.article.domain.dto.ArticlePageDto;
import kr.co.bookand.backend.article.domain.dto.ArticleSearchDto;
import kr.co.bookand.backend.article.service.ArticleService;
import kr.co.bookand.backend.common.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.bookand.backend.article.domain.dto.ArticleDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/article")
@Api(tags = "아티클 API")
public class ArticleController {

    private final ArticleService articleService;

    @ApiOperation(value = "아티클 생성")
    @PostMapping("")
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody ArticleRequest articleDto) {
        ArticleResponse article = articleService.createArticle(articleDto);
        return ResponseEntity.ok(article);
    }

    @ApiOperation(value = "아티클 단일 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getArticle(@PathVariable Long id) {
        ArticleResponse article = articleService.getArticle(id);
        return ResponseEntity.ok(article);
    }

    @ApiOperation(value = "아티클 수정")
    @PutMapping("")
    public ResponseEntity<ArticleResponse> updateArticle(@RequestBody ArticleRequest articleDto) {
        ArticleResponse article = articleService.updateArticle(articleDto);
        return ResponseEntity.ok(article);
    }

    @ApiOperation(value = "아티클 전체 조회")
    @GetMapping("")
    public ResponseEntity<Page<ArticleResponse>> getArticleList(@PageableDefault(size = 10) Pageable pageable) {
        Page<ArticleResponse> articleList = articleService.getArticleList(pageable);
        return ResponseEntity.ok(articleList);
    }

    @ApiOperation(value = "아티클 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Message> removeArticle(@PathVariable Long id) {
        articleService.removeBookStore(id);
        return ResponseEntity.ok(Message.of("아티클 삭제 완료."));
    }

    @ApiOperation(value = "조건에 맞는 아티클 조회")
    @PostMapping("/search")
    public ResponseEntity<ArticlePageDto> findArticleByCriteria(@RequestBody ArticleSearchDto articleSearchDto) {
        return ResponseEntity.ok(articleService.searchArticleList(articleSearchDto));
    }

    @ApiOperation(value = "선택된 서점 삭제")
    @DeleteMapping("/list")
    public ResponseEntity<Message> deleteArticleList(@RequestBody ArticleListDto list) {
        return ResponseEntity.ok(articleService.deleteArticleList(list));
    }
}
