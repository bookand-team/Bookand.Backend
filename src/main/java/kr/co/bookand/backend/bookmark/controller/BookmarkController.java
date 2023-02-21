package kr.co.bookand.backend.bookmark.controller;

import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.bookmark.service.BookmarkService;
import kr.co.bookand.backend.common.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.bookand.backend.bookmark.domain.dto.BookmarkDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @ApiOperation(value = "북마크 폴더 생성")
    @PostMapping("")
    public ResponseEntity<BookmarkResponse> createBookmarkFolder(@RequestBody BookmarkRequest bookmarkRequest) {
        return ResponseEntity.ok(bookmarkService.createBookmarkFolder(bookmarkRequest));
    }

    @ApiOperation(value = "북마크 폴더 리스트 조회")
    @GetMapping("")
    public ResponseEntity<BookmarkFolderListResponse> getBookmarkFolderList(@RequestParam String bookmarkType) {
        return ResponseEntity.ok(bookmarkService.getBookmarkFolderList(bookmarkType));
    }

    @ApiOperation(value = "북마크 상세 조회")
    @GetMapping("/folders/{bookmarkFolderId}")
    public ResponseEntity<BookmarkResponse> getBookmarkFolder(@PathVariable Long bookmarkFolderId) {
        return ResponseEntity.ok(bookmarkService.getBookmarkFolder(bookmarkFolderId));
    }

    @ApiOperation(value = "북마크 폴더 이름 수정")
    @PutMapping("/folders/{bookmarkFolderId}")
    public ResponseEntity<BookmarkResponse> updateBookmarkFolderName(@PathVariable Long bookmarkFolderId, @RequestBody BookmarkFolderNameRequest bookmarkRequest) {
        return ResponseEntity.ok(bookmarkService.updateBookmarkFolderName(bookmarkFolderId, bookmarkRequest));
    }

    @ApiOperation(value = "북마크 폴더에 내용 추가")
    @PostMapping("/folders/{bookmarkFolderId}")
    public ResponseEntity<BookmarkResponse> addBookmark(@PathVariable Long bookmarkFolderId, @RequestBody BookmarkContentListRequest bookmarkRequest) {
        return ResponseEntity.ok(bookmarkService.updateBookmarkFolder(bookmarkFolderId, bookmarkRequest));
    }

    @ApiOperation(value = "모아보기 북마크 보기")
    @GetMapping("/collections")
    public ResponseEntity<BookmarkResponse> getBookmarkCollect(@RequestParam String bookmarkType) {
        return ResponseEntity.ok(bookmarkService.getBookmarkCollect(bookmarkType));
    }

    @ApiOperation(value = "모아보기 북마크 삭제")
    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<Message> deleteBookmark(@PathVariable Long bookmarkId, @RequestBody BookmarkContentListRequest bookmarkRequest) {
        return ResponseEntity.ok(bookmarkService.deleteBookmarkContent(bookmarkId, bookmarkRequest));
    }

    @ApiOperation(value = "북마크 폴더 삭제")
    @DeleteMapping("/folders/{bookmarkFolderId}")
    public ResponseEntity<Message> deleteBookmarkFolder(@PathVariable Long bookmarkFolderId, @RequestBody BookmarkContentListRequest bookmarkRequest) {
        return ResponseEntity.ok(bookmarkService.deleteBookmarkFolderContent(bookmarkFolderId, bookmarkRequest));
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
