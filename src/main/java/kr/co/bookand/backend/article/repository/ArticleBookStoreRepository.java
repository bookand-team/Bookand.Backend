package kr.co.bookand.backend.article.repository;

import kr.co.bookand.backend.article.domain.ArticleBookStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleBookStoreRepository extends JpaRepository<ArticleBookStore, Long> {
}
