package kr.co.bookand.backend.article.service;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.dto.ArticleDto;
import kr.co.bookand.backend.article.exception.NotFoundArticleException;
import kr.co.bookand.backend.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleDto getArticle(String name) {
        return articleRepository.findByTitle(name).map(ArticleDto::of).orElseThrow(() -> new NotFoundArticleException(name));
    }

    public Page<ArticleDto> getArticleList(Pageable pageable) {
        return articleRepository.findAll(pageable).map(ArticleDto::of);
    }

    public ArticleDto createArticle(ArticleDto articleDto) {
        Article article = articleDto.toArticle();
        Article articleSave = articleRepository.save(article);
        return ArticleDto.of(articleSave);
    }

    public ArticleDto updateArticle(ArticleDto articleDto) {
        String name = articleDto.getTitle();
        Article article = articleRepository.findByTitle(name).orElseThrow(()-> new NotFoundArticleException(name));
        article.updateArticle(articleDto);
        return ArticleDto.of(article);
    }

    public void removeBookStore(String name) {
        Article article = articleRepository.findByTitle(name).orElseThrow(() -> new NotFoundArticleException(name));
        articleRepository.delete(article);
    }
}
