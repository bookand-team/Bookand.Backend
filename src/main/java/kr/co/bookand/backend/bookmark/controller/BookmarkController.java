package kr.co.bookand.backend.bookmark.controller;

import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.bookmark.domain.dto.BookmarkDto;
import kr.co.bookand.backend.bookmark.service.BookmarkService;
import lombok.Getter;
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
    @GetMapping("{bookmarkFolderId}")
    public BookmarkResponse getBookmarkFolder(@PathVariable Long bookmarkFolderId) {
        return bookmarkService.getBookmarkFolder(bookmarkFolderId);
    }

    @ApiOperation(value = "북마크 이름 수정")
    @PutMapping("{bookmarkFolderId}")
    public BookmarkResponse updateBookmarkFolderName(@PathVariable Long bookmarkFolderId, @RequestBody BookmarkFolderNameRequest bookmarkRequest) {
        return bookmarkService.updateBookmarkFolderName(bookmarkFolderId, bookmarkRequest);
    }

    @ApiOperation(value = "북마크 내용 추가")
    @PostMapping("{bookmarkFolderId}")
    public BookmarkResponse addBookmark(@PathVariable Long bookmarkFolderId, @RequestBody BookmarkAddContentRequest bookmarkRequest) {
        return bookmarkService.updateBookmarkFolder(bookmarkFolderId, bookmarkRequest);
    }

}
