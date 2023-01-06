package kr.co.bookand.backend.article.repository;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.Category;
import kr.co.bookand.backend.common.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByTitle(String title);

    @Query(value = "SELECT a FROM Article a WHERE a.status = :status  AND a.title LIKE %:title%",
            countQuery = "SELECT count (b) FROM BookStore b")
    Page<Article> findByTitleAndStatus(
            @Param("status") Status status,
            @Param("title") String title,
            Pageable pageable
    );

    Long countBy();

    Long countByCategory(Category category);
}
