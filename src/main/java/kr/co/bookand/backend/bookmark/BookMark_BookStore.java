package kr.co.bookand.backend.bookmark;

import kr.co.bookand.backend.bookstore.BookStore;
import kr.co.bookand.backend.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMark_BookStore extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private BookStore bookStore;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bookmark bookmark;
}
