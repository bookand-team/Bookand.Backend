package kr.co.bookand.backend.article.repository;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.ArticleCategory;
import kr.co.bookand.backend.common.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByTitle(String title);

    boolean existsByTitle(String title);

    Page<Article> findAllByStatus(Status status, Pageable pageable);

    Page<Article> findAllByCategory(ArticleCategory category, Pageable pageable);

    Page<Article> findAllByTitleContaining(String search, Pageable pageable);

    Page<Article>  findAllByCategoryAndStatus(ArticleCategory category, Status status, Pageable pageable);

    Page<Article>  findAllByTitleContainingAndStatus(String search, Status status, Pageable pageable);

    Page<Article>  findAllByTitleContainingAndCategory(String search, ArticleCategory category, Pageable pageable);

    Page<Article>  findAllByTitleContainingAndCategoryAndStatus(String search, ArticleCategory category, Status status, Pageable pageable);
}
