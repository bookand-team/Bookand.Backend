package kr.co.bookand.backend.article.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.Operation
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.article.domain.dto.*
import kr.co.bookand.backend.article.service.ArticleService
import kr.co.bookand.backend.common.domain.MessageResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/articles")
@Api(tags = ["아티클 API"])
class ArticleController(
    private val articleService: ArticleService,
    private val accountService: AccountService
) {
    @ApiOperation(value = "아티클 생성")
    @PostMapping("")
    fun createArticle(
        @RequestBody articleRequest: ArticleRequest
    ): ResponseEntity<ArticleIdResponse> {
        val account = accountService.getCurrentAccount()
        val article = articleService.createArticle(account, articleRequest)
        return ResponseEntity.ok(article)
    }

    @ApiOperation(value = "아티클 상세 조회 (APP)")
    @GetMapping("/{id}")
    fun getArticle(
        @PathVariable id: Long
    ): ResponseEntity<ArticleDetailResponse> {
        val account = accountService.getCurrentAccount()
        val article = articleService.getArticleInfo(account, id)
        return ResponseEntity.ok(article)
    }

    @ApiOperation(value = "아티클 전체 조회 (APP)")
    @Operation(
        summary = "아티클 전체 조회 (APP)", description = """커서 기반으로 되어 있습니다. 
 초기에는 cursorId를 0 넣으시거나 요청 안하시면 됩니다."""
    )
    @GetMapping("")
    fun getSimpleArticleList(
        @PageableDefault(sort = ["modifiedAt"], direction = Sort.Direction.DESC) pageable: Pageable,
        @RequestParam(required = false) cursorId: Long?
    ): ResponseEntity<ArticlePageResponse> {
        val account = accountService.getCurrentAccount()
        val articleList = articleService.getArticleList(account, pageable, cursorId)
        return ResponseEntity.ok(articleList)
    }

    @ApiOperation(value = "아티클 전체 조회 (WEB)")
    @GetMapping("/web")
    fun getArticleList(
        @PageableDefault(sort = ["modifiedAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<ArticleWebPageResponse> {
        val account = accountService.getCurrentAccount()
        val articleList = articleService.getWebArticleList(account, pageable)
        return ResponseEntity.ok(articleList)
    }

    @ApiOperation(value = "조건에 맞는 아티클 조회")
    @GetMapping("/search")
    @ApiImplicitParams(
        ApiImplicitParam(
            name = "search",
            value = "검색어",
            required = false,
            dataType = "string",
            paramType = "query"
        ),
        ApiImplicitParam(
            name = "category",
            value = "카테고리, INTERVIEW, BOOK_REVIEW, BOOKSTORE_REVIEW",
            required = false,
            dataType = "string",
            paramType = "query"
        ),
        ApiImplicitParam(
            name = "status",
            value = "상태, INVISIBLE, VISIBLE, REMOVE",
            required = false,
            dataType = "string",
            paramType = "query"
        )
    )
    fun searchArticleList(
        @RequestParam("search") search: String?,
        @RequestParam("category") category: String?,
        @RequestParam("status") status: String?,
        @PageableDefault(sort = ["modifiedAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<ArticleWebPageResponse> {
        return ResponseEntity.ok(articleService.searchArticleList(search, category, status, pageable))
    }

    @ApiOperation(value = "아티클 수정")
    @PutMapping("/{id}")
    fun updateArticle(
        @PathVariable id: Long,
        @RequestBody articleRequest: ArticleRequest
    ): ResponseEntity<ArticleIdResponse> {
        val account = accountService.getCurrentAccount()
        val article = articleService.updateArticle(account, id, articleRequest)
        return ResponseEntity.ok(article)
    }

    @ApiOperation(value = "아티클 삭제")
    @DeleteMapping("/{id}")
    fun removeArticle(
        @PathVariable id: Long
    ): ResponseEntity<MessageResponse> {
        val account = accountService.getCurrentAccount()
        articleService.removeArticle(account, id)
        return ResponseEntity.ok(MessageResponse(message = "아티클 삭제 완료.", statusCode = 200))
    }

    @ApiOperation(value = "선택된 아티클 삭제")
    @DeleteMapping("/list")
    fun deleteArticleList(
        @RequestBody list: ArticleListRequest
    ): ResponseEntity<MessageResponse> {
        val account = accountService.getCurrentAccount()
        articleService.removeArticleList(account, list)
        return ResponseEntity.ok(MessageResponse(message = "선택된 서점 삭제 완료.", statusCode = 200))
    }

    @ApiOperation(value = "아티클 상태 변경")
    @PutMapping("/{id}/status")
    fun updateArticleStatus(
        @PathVariable id: Long,
    ): ResponseEntity<MessageResponse> {
        return ResponseEntity.ok(articleService.updateArticleStatus(id))
    }
}