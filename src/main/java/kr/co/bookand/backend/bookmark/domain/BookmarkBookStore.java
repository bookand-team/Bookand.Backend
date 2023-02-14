package kr.co.bookand.backend.bookmark.domain;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookmarkBookStore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_bookstore_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private BookStore bookStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    public void updateBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }
}
