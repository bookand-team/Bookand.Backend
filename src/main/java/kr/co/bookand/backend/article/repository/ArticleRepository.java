package kr.co.bookand.backend.article.repository;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.ArticleCategory;
import kr.co.bookand.backend.common.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
    boolean existsByTitle(String title);

    Page<Article> findAllByStatus(Status status, Pageable pageable);

    Long countAllByVisibility(boolean visibility);

    Long countAllByVisibilityAndCategory(boolean visibility, ArticleCategory articleCategory);
}
