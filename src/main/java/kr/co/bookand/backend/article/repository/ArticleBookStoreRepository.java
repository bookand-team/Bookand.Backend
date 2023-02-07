package kr.co.bookand.backend.article.repository;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.ArticleBookStore;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleBookStoreRepository extends JpaRepository<ArticleBookStore, Long> {
    List<ArticleBookStore> findAllByArticleId(Long articleId);
    List<ArticleBookStore> findByArticleAndBookStore(Article article, BookStore bookStore);
}
