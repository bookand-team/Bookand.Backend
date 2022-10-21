package kr.co.bookand.backend.article.service;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.exception.NotFoundArticleException;
import kr.co.bookand.backend.article.repository.ArticleRepository;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto;
import kr.co.bookand.backend.bookstore.exception.NotFoundBookStoreException;
import kr.co.bookand.backend.bookstore.repository.BookStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kr.co.bookand.backend.article.domain.dto.ArticleDto.*;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final BookStoreRepository bookStoreRepository;

    public ArticleResponse getArticle(String name) {
        Article article = articleRepository.findByTitle(name).orElseThrow(() -> new NotFoundArticleException(name));
        return ArticleRequest.of(article);
    }

    public Page<ArticleResponse> getArticleList(Pageable pageable) {
        return articleRepository.findAll(pageable).map(ArticleRequest::of);
    }

    public ArticleResponse createArticle(ArticleRequest articleDto) {
        String bookStoreList = articleDto.getBookStoreList();
        List<Long> bookStoreIdList = Stream.of(bookStoreList.split("\\s*,\\s*")).map(Long::parseLong)
                .collect(Collectors.toList());
        List<BookStoreDto> bookStores = new ArrayList<>();

        for (Long id : bookStoreIdList) {
            BookStoreDto bookStoreDto = bookStoreRepository.findById(id).map(BookStoreDto::of).orElseThrow(() -> new NotFoundBookStoreException(id));
            bookStores.add(bookStoreDto);
        }
        Article article = articleDto.toArticle(bookStores);
        Article articleSave = articleRepository.save(article);

        return ArticleRequest.of(articleSave);
    }

    public ArticleResponse updateArticle(ArticleRequest articleDto) {
        String name = articleDto.getTitle();
        Article article = articleRepository.findByTitle(name).orElseThrow(()-> new NotFoundArticleException(name));
        // 이름 중복 로직
        article.updateArticle(articleDto);
        return ArticleRequest.of(article);
    }

    public void removeBookStore(String name) {
        Article article = articleRepository.findByTitle(name).orElseThrow(() -> new NotFoundArticleException(name));
        articleRepository.delete(article);
    }
}
