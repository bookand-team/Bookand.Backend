package kr.co.bookand.backend.article.service;

import kr.co.bookand.backend.account.service.AccountService;
import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.ArticleBookStore;
import kr.co.bookand.backend.article.domain.ArticleCategory;
import kr.co.bookand.backend.article.domain.ArticleTag;
import kr.co.bookand.backend.article.exception.ArticleException;
import kr.co.bookand.backend.article.repository.ArticleBookStoreRepository;
import kr.co.bookand.backend.article.repository.ArticleRepository;

import kr.co.bookand.backend.article.repository.ArticleTagRepository;
import kr.co.bookand.backend.bookmark.domain.BookmarkType;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

import static kr.co.bookand.backend.article.domain.dto.ArticleDto.*;
import static kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto.*;
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
        List<BookStore> bookStoreList = bookStores.stream()
                .map(bookStoreId -> bookStoreRepository.findById(bookStoreId)
                        .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_BOOKSTORE, bookStoreId)))
                .toList();
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
                .toList();

        saveArticle.updateArticleTagList(tags);
        return getArticleResponse(bookStoreList, saveArticle);
    }

    private ArticleResponse getArticleResponse(List<BookStore> bookStoreList, Article saveArticle) {
        List<BookStoreSimpleResponse> bookStoreSimpleResponse = new ArrayList<>();

        for (BookStore bookStore : bookStoreList) {
            ArticleBookStore articleBookStore = ArticleBookStore.of(saveArticle, bookStore);
            articleBookStoreRepository.save(articleBookStore);
            bookStore.updateArticleBookStore(articleBookStore);
            saveArticle.addArticleBookStore(articleBookStore);
            bookStoreSimpleResponse.add(BookStoreSimpleResponse.of(bookStore, false));
        }
        boolean bookmark = bookmarkService.isBookmark(saveArticle.getId(), BookmarkType.ARTICLE.toString());
        return ArticleResponse.of(saveArticle, bookStoreSimpleResponse, bookmark);
    }

    public void duplicateArticle(String title) {
        if (articleRepository.existsByTitle(title)) {
            throw new ArticleException(ErrorCode.DUPLICATE_ARTICLE, title);
        }
    }

    // 상세 조회 (APP)
    public ArticleResponse getArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, id));
        List<BookStoreSimpleResponse> bookStoreSimpleResponse = new ArrayList<>();
        article.getArticleBookStoreList()
                .stream().filter(articleBookStore -> articleBookStore.getBookStore().getStatus().equals(Status.VISIBLE))
                .forEach(articleBookStore -> {
            BookStore bookStore = articleBookStore.getBookStore();
            boolean isBookmark = bookmarkService.isBookmark(bookStore.getId(), BookmarkType.ARTICLE.toString());
            bookStoreSimpleResponse.add(BookStoreSimpleResponse.of(bookStore, isBookmark));
        });
        boolean bookmark = bookmarkService.isBookmark(id, BookmarkType.ARTICLE.toString());
        return ArticleResponse.of(article, bookStoreSimpleResponse, bookmark);
    }

    // 전체 조회 (APP)
    public ArticleSimplePageResponse getSimpleArticleList(Pageable pageable) {
        Page<ArticleSimpleResponse> articlePage = articleRepository.findAllByStatus(Status.VISIBLE, pageable)
                .map((Article article) -> ArticleSimpleResponse
                        .of(article, bookmarkService.isBookmark(article.getId(), BookmarkType.ARTICLE.toString())));
        return ArticleSimplePageResponse.of(articlePage);
    }

    // 전체 조회 (WEB)
    public ArticleWebPageResponse getArticleList(Pageable pageable) {
        Page<ArticleWebResponse> articlePage = articleRepository.findAll(pageable)
                .map(ArticleWebResponse::of);
        return ArticleWebPageResponse.of(articlePage);
    }

    // 상세 조회 (APP)
    public ArticleSimpleResponse getSimpleArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, id));
        return ArticleSimpleResponse
                .of(article, bookmarkService.isBookmark(article.getId(), BookmarkType.ARTICLE.toString()));
    }

    @Transactional
    public ArticleResponse updateArticle(Long id, ArticleRequest articleRequest) {
        accountService.isAccountAdmin();
        List<Long> longs = articleRequest.bookStoreList();
        List<BookStore> bookStoreList;
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, id));
        bookStoreList = longs.stream().map(bookStoreId -> bookStoreRepository.findById(bookStoreId)
                .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_BOOKSTORE, bookStoreId))).toList();
        Object[] objects = article.getArticleBookStoreList().toArray();
        for (Object object : objects) {
            ArticleBookStore articleBookStore = (ArticleBookStore) object;
            articleBookStore.getArticle().removeArticleAndBookStore(articleBookStore);
            articleBookStore.getBookStore().removeArticleBookStore(articleBookStore);
            articleBookStoreRepository.delete(articleBookStore);
        }
        return getArticleResponse(bookStoreList, article);
    }

    @Transactional
    public ArticleWebPageResponse searchArticleList(PageStateRequest pageStateRequest) {
        Pageable pageable = PageRequest.of(pageStateRequest.page() - 1, pageStateRequest.row());
        String search = pageStateRequest.search();
        String articleCategory = pageStateRequest.category();
        ArticleCategory category = ArticleCategory.valueOf(articleCategory);
        String articleStatus = pageStateRequest.status();
        Status status = Status.valueOf(articleStatus);
        Page<ArticleWebResponse> articlePage;
        if (search == null && category == null && status == null) {
            articlePage = articleRepository.findAll(pageable).map(ArticleWebResponse::of);
        } else if (search == null && category == null) {
            articlePage = articleRepository.findAllByStatus(status, pageable).map(ArticleWebResponse::of);
        } else if (search == null && status == null) {
            articlePage = articleRepository.findAllByCategory(category, pageable).map(ArticleWebResponse::of);
        } else if (category == null && status == null) {
            articlePage = articleRepository.findAllByTitleContaining(search, pageable).map(ArticleWebResponse::of);
        } else if (search == null) {
            articlePage = articleRepository.findAllByCategoryAndStatus(category, status, pageable).map(ArticleWebResponse::of);
        } else if (category == null) {
            articlePage = articleRepository.findAllByTitleContainingAndStatus(search, status, pageable).map(ArticleWebResponse::of);
        } else if (status == null) {
            articlePage = articleRepository.findAllByTitleContainingAndCategory(search, category, pageable).map(ArticleWebResponse::of);
        } else {
            articlePage = articleRepository.findAllByTitleContainingAndCategoryAndStatus(search, category, status, pageable).map(ArticleWebResponse::of);
        }

        return ArticleWebPageResponse.of(articlePage);
    }

    @Transactional
    public void removeBookStore(Long id) {
        accountService.isAccountAdmin();
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, id));
        article.softDelete();
    }

    @Transactional
    public Message deleteArticleList(ArticleListRequest list) {
        accountService.isAccountAdmin();

        for (Long id : list.articleIdList()) {
            Article article = articleRepository.findById(id)
                    .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, id));
            article.softDelete();
        }
        // 예외처리
        return Message.of("삭제완료");
    }

    @Transactional
    public Message updateArticleStatus(Long id) {
        accountService.isAccountAdmin();
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE, id));
        article.updateArticleStatus(article.getStatus() == Status.VISIBLE ? Status.INVISIBLE : Status.VISIBLE);
        return Message.of("상태 변경 완료");
    }
}
