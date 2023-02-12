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
        public static BookmarkResponse of(Bookmark bookmark, Page<BookmarkInfo> bookmarkInfo) {
            return new BookmarkResponse(
                    bookmark.getId(),
                    bookmark.getFolderName(),
                    bookmark.getBookmarkType(),
                    bookmark.getFolderImage(),
                    PageResponse.of(bookmarkInfo)
            );
        }
    }

    public record BookmarkInfo(
            Long id,
            String title,
            String image,
            String location
    ) {
        public static BookmarkInfo ofBookStore(BookStore bookStore) {
            return new BookmarkInfo(
                    bookStore.getId(),
                    bookStore.getName(),
                    bookStore.getMainImage(),
                    bookStore.getAddress()
            );
        }

        public static BookmarkInfo ofArticle(Article article) {
            return new BookmarkInfo(
                    article.getId(),
                    article.getTitle(),
                    "image",
                    "location"
            );
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
        public static BookmarkFolderResponse of(Bookmark bookmark) {
            return new BookmarkFolderResponse(
                    bookmark.getId(),
                    bookmark.getFolderName(),
                    bookmark.getBookmarkType(),
                    bookmark.getFolderImage()
            );
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
