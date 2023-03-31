package kr.co.bookand.backend.bookmark.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import kr.co.bookand.backend.bookmark.service.BookmarkService;
import kr.co.bookand.backend.common.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.bookand.backend.bookmark.domain.dto.BookmarkDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
@Api(tags = "북마크 API")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @ApiOperation(value = "북마크 폴더 생성")
    @Operation(description = "북마크 폴더 생성" +
            "종류는 BOOKSTORE/ARTICLE")
    @PostMapping("")
    public ResponseEntity<BookmarkResponseId> createBookmarkFolder(@RequestBody BookmarkRequest bookmarkRequest) {
        return ResponseEntity.ok(bookmarkService.createBookmarkFolder(bookmarkRequest));
    }

    @ApiOperation(value = "북마크 폴더 리스트 조회")
    @ApiImplicitParam(name = "bookmarkType", value = "북마크 종류, BOOKSTORE/ARTICLE", required = true, dataType = "string", paramType = "query")
    @GetMapping("")
    public ResponseEntity<BookmarkFolderListResponse> getBookmarkFolderList(@RequestParam String bookmarkType) {
        return ResponseEntity.ok(bookmarkService.getBookmarkFolderList(bookmarkType));
    }

    @ApiOperation(value = "북마크 상세 조회")
    @Operation(summary = "북마크 상세 조회", description = "커서 기반으로 되어 있습니다. " +
            "\n 초기에는 cursorId를 0 넣으시거나 요청 안하시면 됩니다.")
    @GetMapping("/folders/{bookmarkFolderId}")
    public ResponseEntity<BookmarkResponse> getBookmarkFolder(
            @PathVariable Long bookmarkFolderId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        return ResponseEntity.ok(bookmarkService.getBookmarkFolder(bookmarkFolderId, pageable, cursorId));
    }

    @ApiOperation(value = "북마크 폴더 이름 수정")
    @PutMapping("/folders/{bookmarkFolderId}")
    public ResponseEntity<BookmarkResponseId> updateBookmarkFolderName(
            @PathVariable Long bookmarkFolderId,
            @RequestBody BookmarkFolderNameRequest bookmarkRequest
    ) {
        return ResponseEntity.ok(bookmarkService.updateBookmarkFolderName(bookmarkFolderId, bookmarkRequest));
    }

    @ApiOperation(value = "북마크 폴더에 내용 추가")
    @PostMapping("/folders/{bookmarkFolderId}")
    public ResponseEntity<BookmarkResponseId> addBookmark(
            @PathVariable Long bookmarkFolderId,
            @RequestBody BookmarkContentListRequest bookmarkRequest
    ) {
        return ResponseEntity.ok(bookmarkService.updateBookmarkFolder(bookmarkFolderId, bookmarkRequest));
    }

    @ApiOperation(value = "모아보기 북마크 보기")
    @Operation(summary = "북마크 보기", description = "커서 기반으로 되어 있습니다. " +
            "\n 초기에는 cursorId를 0 넣으시거나 요청 안하시면 됩니다.")
    @ApiImplicitParam(name = "bookmarkType", value = "북마크 종류, BOOKSTORE/ARTICLE", required = true, dataType = "string", paramType = "query")
    @GetMapping("/collections")
    public ResponseEntity<BookmarkResponse> getBookmarkCollect(
            @RequestParam String bookmarkType,
            @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        return ResponseEntity.ok(bookmarkService.getBookmarkCollect(bookmarkType, pageable, cursorId));
    }

    @ApiOperation(value = "모아보기 북마크 삭제")
    @Operation(description = "북마크 폴더 삭제" +
            "종류는 BOOKSTORE/ARTICLE" +
            "\n 삭제할 북마크들은 리스트로 넘겨주시면 됩니다."
    )
    @DeleteMapping("/collections")
    public ResponseEntity<Message> deleteBookmark(@RequestBody BookmarkContentListRequest bookmarkRequest) {
        return ResponseEntity.ok(bookmarkService.deleteBookmarkContent(bookmarkRequest));
    }

    @ApiOperation(value = "북마크 폴더 내용 삭제")
    @Operation(description = "북마크 폴더 내용 삭제" +
            "종류는 BOOKSTORE/ARTICLE" +
            "\n 삭제할 북마크들은 리스트로 넘겨주시면 됩니다."
    )
    @DeleteMapping("/folders/{bookmarkFolderId}/contents")
    public ResponseEntity<Message> deleteBookmarkFolder(
            @PathVariable Long bookmarkFolderId,
            @RequestBody BookmarkContentListRequest bookmarkRequest
    ) {
        return ResponseEntity.ok(bookmarkService.deleteBookmarkFolderContent(bookmarkFolderId, bookmarkRequest));
    }

    @ApiOperation(value = "북마크 폴더 삭제")
    @Operation(description = "북마크 폴더 삭제" +
            "폴더 id 만 입력하면 됩니다. (타입 구분 X)"
    )
    @DeleteMapping("/folders/{bookmarkFolderId}")
    public ResponseEntity<Message> deleteBookmarkFolder(
            @PathVariable Long bookmarkFolderId
    ) {
        return ResponseEntity.ok(bookmarkService.deleteBookmarkFolder(bookmarkFolderId));
    }


    @ApiOperation(value = "아티클 북마크 추가")
    @PostMapping("/articles/{articleId}")
    public ResponseEntity<Message> createArticleBookmark(@PathVariable Long articleId) {
        return ResponseEntity.ok(bookmarkService.createArticleBookmark(articleId));
    }

    @ApiOperation(value = "서점 북마크 추가")
    @PostMapping("/bookstores/{bookstoreId}")
    public ResponseEntity<Message> createStoreBookmark(@PathVariable Long bookstoreId) {
        return ResponseEntity.ok(bookmarkService.createBookStoreBookmark(bookstoreId));
    }
}
