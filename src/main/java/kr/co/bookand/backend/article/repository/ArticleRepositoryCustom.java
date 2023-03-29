package kr.co.bookand.backend.article.repository;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.common.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {
    Page<Article> findAllBySearch(String search, String category, String status, Pageable pageable);

    Page<Article> findAllByStatus(Status status, Pageable pageable, Long cursorId, String date);
}
