package kr.co.bookand.backend.article.domain;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ArticleBookStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_bookstore_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookstore_id")
    private BookStore bookStore;

    public void updateArticleAndBookStore(Article article, BookStore bookStore) {
        this.article = article;
        this.bookStore = bookStore;
    }

    public void addArticleAndBookStore(Article article, BookStore bookStore) {
        this.article = article;
        this.bookStore = bookStore;
    }

    public static ArticleBookStore of(Article article, BookStore bookStore) {
        return ArticleBookStore.builder()
                .article(article)
                .bookStore(bookStore)
                .build();
    }

    public void removeArticleAndBookStore() {
        this.article = null;
        this.bookStore = null;
    }
}
