package kr.co.bookand.backend.article.service;

import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.dto.ArticleDto;
import kr.co.bookand.backend.article.domain.dto.ArticleListDto;
import kr.co.bookand.backend.article.domain.dto.ArticlePageDto;
import kr.co.bookand.backend.article.domain.dto.ArticleSearchDto;
import kr.co.bookand.backend.article.exception.ArticleException;
import kr.co.bookand.backend.article.repository.ArticleRepository;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto;
import kr.co.bookand.backend.bookstore.exception.BookStoreException;
import kr.co.bookand.backend.bookstore.repository.BookStoreRepository;
import kr.co.bookand.backend.common.ErrorCode;
import kr.co.bookand.backend.common.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public ArticleResponse getArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, id));
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
            BookStoreDto bookStoreDto = bookStoreRepository.findById(id).map(BookStoreDto::of).orElseThrow(() -> new BookStoreException(ErrorCode.NOT_FOUND_BOOKSTORE, id));
            bookStores.add(bookStoreDto);
        }
        Article article = articleDto.toArticle(bookStores);
        Article articleSave = articleRepository.save(article);

        return ArticleRequest.of(articleSave);
    }

    public ArticleResponse updateArticle(ArticleRequest articleDto) {
        Long articleDtoId = articleDto.getId();
        Article article = articleRepository.findById(articleDtoId).orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, articleDtoId));
        article.updateArticle(articleDto);
        return ArticleRequest.of(article);
    }

    public void removeBookStore(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, id));
        articleRepository.delete(article);
    }

    public ArticlePageDto searchArticleList(ArticleSearchDto articleSearchDto) {
        List<ArticleResponse> result = new ArrayList<>();
        Pageable pageable = PageRequest.of(articleSearchDto.getPage(), articleSearchDto.getRaw());
        Page<Article> byTitleAndStatus = articleRepository.findByTitleAndStatus(articleSearchDto.getStatus(), articleSearchDto.getSearch(), pageable);
        byTitleAndStatus
                .forEach(article -> result.add(ArticleDto.ArticleRequest.of(article)));

        int totalPages = byTitleAndStatus.getTotalPages();
        int number = byTitleAndStatus.getNumber();

        return ArticlePageDto.of(result, totalPages, number);
    }

    public Message deleteArticleList(ArticleListDto list) {
        list.getArticleDtoList().forEach(articleRepository::deleteById);
        // 예외처리
        return Message.of("삭제완료");
    }
}
