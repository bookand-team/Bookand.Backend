package kr.co.bookand.backend.bookmark.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.Operation
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.bookmark.domain.dto.*
import kr.co.bookand.backend.bookmark.service.BookmarkService
import kr.co.bookand.backend.common.domain.MessageResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/bookmarks")
@Api(tags = ["북마크 API"])
class KotlinBookmarkController(
    private val bookmarkService: BookmarkService,
    private val accountService: AccountService
) {

    @ApiOperation(value = "북마크 폴더 생성")
    @Operation(
        description = "북마크 폴더 생성 종류는 BOOKSTORE/ARTICLE"
    )
    @PostMapping("")
    fun createBookmarkFolder(
        @RequestBody bookmarkRequest: BookmarkFolderRequest
    ): ResponseEntity<BookmarkIdResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookmarkService.createBookmarkFolder(account, bookmarkRequest))
    }

    @ApiOperation(value = "북마크 폴더 리스트 조회")
    @ApiImplicitParam(
        name = "bookmarkType",
        value = "북마크 종류, BOOKSTORE/ARTICLE",
        required = true,
        dataType = "string",
        paramType = "query"
    )
    @GetMapping("")
    fun getBookmarkFolderList(
        @RequestParam bookmarkType: String
    ): ResponseEntity<BookmarkFolderListResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookmarkService.getBookmarkFolderList(account, bookmarkType))
    }

    @ApiOperation(value = "북마크 상세 조회")
    @Operation(
        summary = "북마크 상세 조회",
        description = "커서 기반으로 되어 있습니다. 초기에는 cursorId를 0 넣으시거나 요청 안하시면 됩니다."
    )
    @GetMapping("/folders/{bookmarkFolderId}")
    fun getBookmarkFolder(
        @PathVariable bookmarkFolderId: Long,
        @PageableDefault(size = 10, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable,
        @RequestParam(required = false) cursorId: Long?
    ): ResponseEntity<BookmarkResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookmarkService.getBookmarkFolder(account, bookmarkFolderId, pageable, cursorId)
        )
    }

    @ApiOperation(value = "북마크 폴더 이름 수정")
    @PutMapping("/folders/{bookmarkFolderId}")
    fun updateBookmarkFolderName(
        @PathVariable bookmarkFolderId: Long,
        @RequestBody bookmarkRequest: BookmarkFolderNameRequest
    ): ResponseEntity<BookmarkIdResponse> {
        val account = accountService.getCurrentAccount()
        val updateBookmarkFolder = bookmarkService.updateBookmarkFolderName(account, bookmarkFolderId, bookmarkRequest)
        return ResponseEntity.ok(updateBookmarkFolder)
    }

    @ApiOperation(value = "북마크 폴더에 내용 추가")
    @PostMapping("/folders/{bookmarkFolderId}")
    fun addBookmark(
        @PathVariable bookmarkFolderId: Long,
        @RequestBody bookmarkRequest: BookmarkContentListRequest
    ): ResponseEntity<BookmarkIdResponse> {
        val account = accountService.getCurrentAccount()
        val updateBookmarkFolder = bookmarkService.updateBookmarkFolder(account, bookmarkFolderId, bookmarkRequest)
        return ResponseEntity.ok(updateBookmarkFolder)
    }

    @ApiOperation(value = "모아보기 북마크 보기")
    @Operation(
        summary = "북마크 보기",
        description = "커서 기반으로 되어 있습니다. 초기에는 cursorId를 0 넣으시거나 요청 안하시면 됩니다."
    )
    @ApiImplicitParam(
        name = "bookmarkType",
        value = "북마크 종류, BOOKSTORE/ARTICLE",
        required = true,
        dataType = "string",
        paramType = "query"
    )
    @GetMapping("/collections")
    fun getBookmarkCollect(
        @RequestParam bookmarkType: String,
        @PageableDefault(sort = ["modifiedAt"], direction = Sort.Direction.DESC) pageable: Pageable,
        @RequestParam(required = false) cursorId: Long?
    ): ResponseEntity<BookmarkResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookmarkService.getBookmarkCollect(account, bookmarkType, pageable, cursorId))
    }

    @ApiOperation(value = "모아보기 북마크 삭제")
    @Operation(
        description = "북마크 폴더 삭제종류는 BOOKSTORE/ARTICLE 삭제할 북마크들은 리스트로 넘겨주시면 됩니다."
    )
    @DeleteMapping("/collections")
    fun deleteBookmark(
        @RequestBody bookmarkRequest: BookmarkContentListRequest
    ): ResponseEntity<MessageResponse> {
        val account = accountService.getCurrentAccount()
        bookmarkService.deleteInitBookmarkContent(account, bookmarkRequest)
        return ResponseEntity.ok(MessageResponse(message = "북마크 삭제 완료", statusCode = 200))
    }

    @ApiOperation(value = "북마크 폴더 내용 삭제")
    @Operation(
        description = "북마크 폴더 내용 삭제종류는 BOOKSTORE/ARTICLE 삭제할 북마크들은 리스트로 넘겨주시면 됩니다."
    )
    @DeleteMapping("/folders/{bookmarkFolderId}/contents")
    fun deleteBookmarkFolder(
        @PathVariable bookmarkFolderId: Long,
        @RequestBody bookmarkRequest: BookmarkContentListRequest
    ): ResponseEntity<MessageResponse> {
        val account = accountService.getCurrentAccount()
        bookmarkService.deleteBookmarkContent(account, bookmarkFolderId, bookmarkRequest)
        return ResponseEntity.ok(MessageResponse(message = "북마크 삭제 완료", statusCode = 200))
    }

    @ApiOperation(value = "북마크 폴더 삭제")
    @Operation(
        description = "북마크 폴더 삭제" +
                "폴더 id 만 입력하면 됩니다. (타입 구분 X)"
    )
    @DeleteMapping("/folders/{bookmarkFolderId}")
    fun deleteBookmarkFolder(
        @PathVariable bookmarkFolderId: Long
    ): ResponseEntity<MessageResponse> {
        val account = accountService.getCurrentAccount()
        bookmarkService.deleteBookmarkFolder(account, bookmarkFolderId)
        return ResponseEntity.ok(MessageResponse(message = "북마크 폴더 삭제 완료", statusCode = 200))
    }


    @ApiOperation(value = "아티클 북마크 추가")
    @PostMapping("/articles/{articleId}")
    fun createArticleBookmark(
        @PathVariable articleId: Long
    ): ResponseEntity<MessageResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookmarkService.createBookmarkedArticle(account, articleId))
    }

    @ApiOperation(value = "서점 북마크 추가")
    @PostMapping("/bookstores/{bookstoreId}")
    fun createStoreBookmark(
        @PathVariable bookstoreId: Long)
    : ResponseEntity<MessageResponse> {
        val account = accountService.getCurrentAccount()
        return ResponseEntity.ok(bookmarkService.createBookmarkedBookstore(account, bookstoreId))
    }
}