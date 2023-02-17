package kr.co.bookand.backend.article.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.ArticleBookStore;
import kr.co.bookand.backend.article.domain.ArticleCategory;
import kr.co.bookand.backend.article.domain.ArticleTag;
import kr.co.bookand.backend.article.exception.ArticleException;
import kr.co.bookand.backend.article.repository.ArticleBookStoreRepository;
import kr.co.bookand.backend.article.repository.ArticleRepository;

import kr.co.bookand.backend.article.repository.ArticleTagRepository;
import kr.co.bookand.backend.bookmark.domain.BookmarkArticle;
import kr.co.bookand.backend.bookmark.service.BookmarkService;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.repository.BookStoreRepository;
import kr.co.bookand.backend.common.domain.Status;

import kr.co.bookand.backend.common.exception.ErrorCode;
import kr.co.bookand.backend.common.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

import static kr.co.bookand.backend.article.domain.dto.ArticleDto.*;
import static kr.co.bookand.backend.common.domain.dto.PageStateDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final AccountService accountService;
    private final BookStoreRepository bookStoreRepository;
    private final ArticleBookStoreRepository articleBookStoreRepository;
    private final ArticleTagRepository articleTagRepository;
    private final BookmarkService bookmarkService;

    @Transactional
    public ArticleResponse createArticle(ArticleRequest articleRequest) {
        accountService.isAccountAdmin();
        duplicateArticle(articleRequest.title());
        List<Long> bookStores = articleRequest.bookStoreList();
        List<BookStore> bookStoreList = bookStores.stream().map(bookStoreId -> bookStoreRepository.findById(bookStoreId).orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_BOOKSTORE, bookStoreId))).collect(Collectors.toList());
        Article article = articleRequest.toEntity();
        Article saveArticle = articleRepository.save(article);

        List<ArticleTag> tags = articleRequest.tags().stream()
                .map((String id) -> {
                    ArticleTag articleTag = ArticleTag.builder()
                            .tag(id)
                            .article(saveArticle)
                            .build();
                    articleTagRepository.save(articleTag);
                    return articleTag;
                })
                .collect(Collectors.toList());

        saveArticle.updateArticleTagList(tags);
        for (BookStore bookStore : bookStoreList) {
            ArticleBookStore articleBookStore = ArticleBookStore.of(saveArticle, bookStore);
            articleBookStoreRepository.save(articleBookStore);
            bookStore.updateArticleBookStore(articleBookStore);
            saveArticle.addArticleBookStore(articleBookStore);
        }
        return ArticleResponse.of(saveArticle);
    }

    public void duplicateArticle(String title) {
        if (articleRepository.existsByTitle(title)) {
            throw new ArticleException(ErrorCode.DUPLICATE_ARTICLE, title);
        }
    }

    public ArticleResponse getArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, id));
        return ArticleResponse.of(article);
    }

    public ArticlePageResponse getArticleList(Pageable pageable) {
        Page<ArticleResponse> articlePage = articleRepository.findAll(pageable).map(ArticleResponse::of);
        return ArticlePageResponse.of(articlePage);
    }

    public ArticleSimplePageResponse getSimpleArticleList(Pageable pageable) {
        Page<ArticleSimpleResponse> articlePage = articleRepository.findAll(pageable).map(
                (Article article) -> ArticleSimpleResponse.of(article, bookmarkService.isBookmark(article.getId(), "article"))
        );
        return ArticleSimplePageResponse.of(articlePage);
    }

    @Transactional
    public ArticleResponse updateArticle(Long id, ArticleRequest articleRequest) {
        accountService.isAccountAdmin();

        List<Long> longs = articleRequest.bookStoreList();
        List<BookStore> bookStoreList;
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, id));
        bookStoreList = longs.stream().map(bookStoreId -> bookStoreRepository.findById(bookStoreId).orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_BOOKSTORE, bookStoreId))).collect(Collectors.toList());
        Object[] objects = article.getArticleBookStoreList().toArray();
        for (Object object : objects) {
            ArticleBookStore articleBookStore = (ArticleBookStore) object;
            articleBookStore.getArticle().removeArticleAndBookStore(articleBookStore);
            articleBookStore.getBookStore().removeArticleBookStore(articleBookStore);
            articleBookStoreRepository.delete(articleBookStore);
        }

        for (BookStore bookStore : bookStoreList) {
            ArticleBookStore articleBookStore1 = ArticleBookStore.of(article, bookStore);
            articleBookStoreRepository.save(articleBookStore1);
            bookStore.updateArticleBookStore(articleBookStore1);
            article.addArticleBookStore(articleBookStore1);
        }
        return ArticleResponse.of(article);
    }

    @Transactional
    public ArticlePageResponse searchArticleList(PageStateRequest pageStateRequest) {
        Pageable pageable = PageRequest.of(pageStateRequest.page() - 1, pageStateRequest.row());
        String search = pageStateRequest.search();
        String articleCategory = pageStateRequest.category();
        ArticleCategory category = ArticleCategory.valueOf(articleCategory);
        String articleStatus = pageStateRequest.status();
        Status status = Status.valueOf(articleStatus);
        Page<ArticleResponse> articlePage;
        if (search == null && category == null && status == null) {
            articlePage = articleRepository.findAll(pageable).map(ArticleResponse::of);
        } else if (search == null && category == null) {
            articlePage = articleRepository.findAllByStatus(status, pageable).map(ArticleResponse::of);
        } else if (search == null && status == null) {
            articlePage = articleRepository.findAllByCategory(category, pageable).map(ArticleResponse::of);
        } else if (category == null && status == null) {
            articlePage = articleRepository.findAllByTitleContaining(search, pageable).map(ArticleResponse::of);
        } else if (search == null) {
            articlePage = articleRepository.findAllByCategoryAndStatus(category, status, pageable).map(ArticleResponse::of);
        } else if (category == null) {
            articlePage = articleRepository.findAllByTitleContainingAndStatus(search, status, pageable).map(ArticleResponse::of);
        } else if (status == null) {
            articlePage = articleRepository.findAllByTitleContainingAndCategory(search, category, pageable).map(ArticleResponse::of);
        } else {
            articlePage = articleRepository.findAllByTitleContainingAndCategoryAndStatus(search, category, status, pageable).map(ArticleResponse::of);
        }

        return ArticlePageResponse.of(articlePage);
    }

    @Transactional
    public void removeBookStore(Long id) {
        accountService.isAccountAdmin();
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, id));
        article.softDelete();
    }

    @Transactional
    public Message deleteArticleList(ArticleListRequest list) {
        accountService.isAccountAdmin();

        for (Long id : list.articleIdList()) {
            Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, id));
            article.softDelete();
        }
        // 예외처리
        return Message.of("삭제완료");
    }

    @Transactional
    public ArticleResponse updateArticleStatus(Long id) {
        accountService.isAccountAdmin();
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, id));
        article.updateArticleStatus(article.getStatus() == Status.VISIBLE ? Status.INVISIBLE : Status.VISIBLE);
        return ArticleResponse.of(article);
    }
}
