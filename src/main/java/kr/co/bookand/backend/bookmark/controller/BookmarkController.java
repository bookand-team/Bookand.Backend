package kr.co.bookand.backend.bookmark.controller;

import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.bookmark.service.BookmarkService;
import kr.co.bookand.backend.common.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static kr.co.bookand.backend.bookmark.domain.dto.BookmarkDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @ApiOperation(value = "북마크 생성")
    @PostMapping("")
    public BookmarkResponse createBookmarkFolder(@RequestBody BookmarkRequest bookmarkRequest) {
        return bookmarkService.createBookmarkFolder(bookmarkRequest);
    }

    @ApiOperation(value = "북마크 폴더 리스트 조회")
    @GetMapping("")
    public BookmarkFolderListResponse getBookmarkFolderList(@RequestParam String bookmarkType) {
        return bookmarkService.getBookmarkFolderList(bookmarkType);
    }

    @ApiOperation(value = "북마크 내용 조회")
    @GetMapping("/folders/{bookmarkFolderId}")
    public BookmarkResponse getBookmarkFolder(@PathVariable Long bookmarkFolderId) {
        return bookmarkService.getBookmarkFolder(bookmarkFolderId);
    }

    @ApiOperation(value = "북마크 폴더 이름 수정")
    @PutMapping("/folders/{bookmarkFolderId}")
    public BookmarkResponse updateBookmarkFolderName(@PathVariable Long bookmarkFolderId, @RequestBody BookmarkFolderNameRequest bookmarkRequest) {
        return bookmarkService.updateBookmarkFolderName(bookmarkFolderId, bookmarkRequest);
    }

    @ApiOperation(value = "북마크 폴더에 내용 추가")
    @PostMapping("/folders/{bookmarkFolderId}")
    public BookmarkResponse addBookmark(@PathVariable Long bookmarkFolderId, @RequestBody BookmarkContentListRequest bookmarkRequest) {
        return bookmarkService.updateBookmarkFolder(bookmarkFolderId, bookmarkRequest);
    }

    @ApiOperation(value = "모아보기 북마크 보기")
    @GetMapping("/{bookmarkFolderId}")
    public BookmarkResponse getBookmarkCollect(@PathVariable Long bookmarkFolderId) {
        return bookmarkService.getBookmarkCollect(bookmarkFolderId);
    }

    @ApiOperation(value = "모아보기 북마크 삭제")
    @DeleteMapping("/{bookmarkFolderId}")
    public Message deleteBookmark(@PathVariable Long bookmarkFolderId, @RequestBody BookmarkContentListRequest bookmarkRequest) {
        return bookmarkService.deleteBookmarkContent(bookmarkFolderId, bookmarkRequest);
    }

    @ApiOperation(value = "북마크 폴더 삭제")
    @DeleteMapping("/folders/{bookmarkFolderId}")
    public Message deleteBookmarkFolder(@PathVariable Long bookmarkFolderId, @RequestBody BookmarkContentListRequest bookmarkRequest) {
        return bookmarkService.deleteBookmarkFolderContent(bookmarkFolderId, bookmarkRequest);
    }

}
