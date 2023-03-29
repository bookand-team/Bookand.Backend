package kr.co.bookand.backend.article.repository;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.ArticleCategory;
import kr.co.bookand.backend.common.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
    boolean existsByTitle(String title);

    Long countAllByVisibility(boolean visibility);

    Long countAllByVisibilityAndCategory(boolean visibility, ArticleCategory articleCategory);

    Optional<Article> findFirstByStatusOrderByCreatedAtDesc(Status status);

    Long countAllByStatus(Status status);
}
