package kr.co.bookand.backend.bookstore.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class BookStoreTheme {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private BookStoreType theme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookstore_id")
    private BookStore bookStore;

    public void updateBookStore(BookStore bookStore) {
        this.bookStore = bookStore;
    }


}
