package kr.co.bookand.backend.bookmark;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.common.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMarkBookStore extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private BookStore bookStore;

    @ManyToOne(fetch = FetchType.LAZY)
    private BookMark bookmark;
}
