package kr.co.bookand.backend.article.controller;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import kr.co.bookand.backend.article.service.ArticleService;
import kr.co.bookand.backend.common.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.bookand.backend.article.domain.dto.ArticleDto.*;
import static kr.co.bookand.backend.common.domain.dto.PageStateDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
@Api(tags = "아티클 API")
public class ArticleController {

    private final ArticleService articleService;

    @ApiOperation(value = "아티클 생성")
    @PostMapping("")
    public ResponseEntity<ArticleResponse> createArticle(
            @RequestBody ArticleRequest articleRequest
    ) {
        ArticleResponse article = articleService.createArticle(articleRequest);
        return ResponseEntity.ok(article);
    }

    @ApiOperation(value = "아티클 상세 조회 (APP)")
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getArticle(
            @PathVariable Long id
    ) {
        ArticleResponse article = articleService.getArticle(id);
        return ResponseEntity.ok(article);
    }

    @ApiOperation(value = "아티클 전체 조회 (APP)")
    @Operation(summary = "아티클 전체 조회 (APP)", description = "커서 기반으로 되어 있습니다. " +
            "\n 초기에는 cursorId를 0 넣으시거나 요청 안하시면 됩니다.")
    @GetMapping("")
    public ResponseEntity<ArticleSimplePageResponse> getSimpleArticleList(
            @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        ArticleSimplePageResponse articleList = articleService.getSimpleArticleList(pageable, cursorId);
        return ResponseEntity.ok(articleList);
    }

    @ApiOperation(value = "아티클 전체 조회 (WEB)")
    @GetMapping("/web")
    public ResponseEntity<ArticleWebPageResponse> getArticleList(
            @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        ArticleWebPageResponse articleList = articleService.getArticleList(pageable);
        return ResponseEntity.ok(articleList);
    }

    @ApiOperation(value = "조건에 맞는 아티클 조회")
    @GetMapping("/search")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "검색어", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "category", value = "카테고리, INTERVIEW, BOOK_REVIEW, BOOKSTORE_REVIEW", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "상태, INVISIBLE, VISIBLE, REMOVE;", required = false, dataType = "string", paramType = "query")
    })
    public ResponseEntity<ArticleWebPageResponse> searchArticleList(
            @RequestParam("search") String search,
            @RequestParam("category") String category,
            @RequestParam("status") String status,
            @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(articleService.searchArticleList(search, category, status, pageable));
    }

    @ApiOperation(value = "아티클 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleRequest articleRequest
    ) {
        ArticleResponse article = articleService.updateArticle(id, articleRequest);
        return ResponseEntity.ok(article);
    }

    @ApiOperation(value = "아티클 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Message> removeArticle(
            @PathVariable Long id
    ) {
        articleService.removeBookStore(id);
        return ResponseEntity.ok(Message.of("아티클 삭제 완료."));
    }

    @ApiOperation(value = "선택된 서점 삭제")
    @DeleteMapping("/list")
    public ResponseEntity<Message> deleteArticleList(
            @RequestBody ArticleListRequest list
    ) {
        return ResponseEntity.ok(articleService.deleteArticleList(list));
    }

    @ApiOperation(value = "아티클 상태 변경")
    @PutMapping("/{id}/status")
    public ResponseEntity<Message> updateArticleStatus(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(articleService.updateArticleStatus(id));
    }
}
