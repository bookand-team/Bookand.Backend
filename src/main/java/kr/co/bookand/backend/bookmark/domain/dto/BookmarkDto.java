package kr.co.bookand.backend.bookmark.domain.dto;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.bookmark.domain.Bookmark;
import kr.co.bookand.backend.bookmark.domain.BookmarkType;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

public class BookmarkDto {

    public record BookmarkRequest(
            String folderName,
            String bookmarkType
    ) {

        public Bookmark toEntity(Account account) {
            return Bookmark.builder()
                    .account(account)
                    .folderName(folderName)
                    .bookmarkType(BookmarkType.valueOf(bookmarkType))
                    .build();
        }
    }

    public record BookmarkResponse(
            Long bookmarkId,
            String folderName,
            BookmarkType bookmarkType,
            String bookmarkImage,
            PageResponse<BookmarkInfo> bookmarkInfo
    ) {
        @Builder
        public BookmarkResponse{
        }
        public static BookmarkResponse of(Bookmark bookmark, Page<BookmarkInfo> bookmarkInfo) {
            return BookmarkResponse.builder()
                    .bookmarkId(bookmark.getId())
                    .folderName(bookmark.getFolderName())
                    .bookmarkType(bookmark.getBookmarkType())
                    .bookmarkImage(bookmark.getFolderImage())
                    .bookmarkInfo(PageResponse.of(bookmarkInfo))
                    .build();
        }
    }

    public record BookmarkInfo(
            Long bookmarkId,
            String title,
            String image,
            String location
    ) {
        @Builder
        public BookmarkInfo {
        }
        public static BookmarkInfo ofBookStore(BookStore bookStore) {
            return BookmarkInfo.builder()
                    .bookmarkId(bookStore.getId())
                    .title(bookStore.getName())
                    .image(bookStore.getMainImage())
                    .location(bookStore.getAddress())
                    .build();
        }

        public static BookmarkInfo ofArticle(Article article) {
            return BookmarkInfo.builder()
                    .bookmarkId(article.getId())
                    .title(article.getTitle())
                    .image(article.getMainImage())
                    .location("location")
                    .build();
        }

    }

    public record BookmarkFolderListResponse(
            List<BookmarkFolderResponse> bookmarkFolderList
    ) {
    }

    public record BookmarkFolderResponse(
            Long bookmarkId,
            String folderName,
            BookmarkType bookmarkType,
            String bookmarkImage
    ) {
        @Builder
        public BookmarkFolderResponse {
        }
        public static BookmarkFolderResponse of(Bookmark bookmark) {
            return BookmarkFolderResponse.builder()
                    .bookmarkId(bookmark.getId())
                    .folderName(bookmark.getFolderName())
                    .bookmarkType(bookmark.getBookmarkType())
                    .bookmarkImage(bookmark.getFolderImage())
                    .build();
        }
    }

    public record BookmarkContentListRequest(
            List<Long> contentIdList,
            BookmarkType bookmarkType
    ) {
    }

    public record BookmarkFolderNameRequest(
            String folderName
    ) {
    }
}
