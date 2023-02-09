package kr.co.bookand.backend.bookmark;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMarkBookStore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private BookStore bookStore;

    @ManyToOne(fetch = FetchType.LAZY)
    private BookMark bookmark;
}
