package kr.co.bookand.backend.common.service;


import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.ArticleBookStore;
import kr.co.bookand.backend.article.domain.ArticleCategory;
import kr.co.bookand.backend.article.repository.ArticleBookStoreRepository;
import kr.co.bookand.backend.article.repository.ArticleRepository;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.BookstoreTheme;
import kr.co.bookand.backend.bookstore.repository.BookStoreRepository;
import kr.co.bookand.backend.common.domain.DeviceOSFilter;
import kr.co.bookand.backend.common.domain.MemberIdFilter;
import kr.co.bookand.backend.common.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@RequiredArgsConstructor
public class ArticleBookStoreDummyData {

    private final ArticleBookStoreRepository articleBookStoreRepository;
    private final BookStoreRepository bookStoreRepository;
    private final ArticleRepository articleRepository;

    @PostConstruct
    @Transactional
    public void dummyDates() {
        dummyData1();
        dummyData2();
        dummyData3();
    }

    public void dummyData1() {

        for (int i = 0; i < 15; i++) {
            BookStore store = BookStore.builder()
                    .name("name%d".formatted(i))
                    .address("address%d".formatted(i))
                    .businessHours("businessHours%d".formatted(i))
                    .contact("contact%d".formatted(i))
                    .facility("facility%d".formatted(i))
                    .sns("sns%d".formatted(i))
                    .theme(BookstoreTheme.valueOf("TRAVEL"))
                    .mainImage("mainImage%d".formatted(i))
                    .introduction("introduction%d".formatted(i))
                    .status(Status.valueOf("VISIBLE"))
                    .view(1)
                    .build();
            bookStoreRepository.save(store);
        }

    }

    public void dummyData2() {
        for (int i = 0; i < 15; i++) {
            Article article = Article.builder()
                    .title("title%d".formatted(i))
                    .content("content%d".formatted(i))
                    .writer("writer%d".formatted(i))
                    .category(ArticleCategory.BOOK_REVIEW)
                    .memberIdFilter(MemberIdFilter.ALL)
                    .deviceOSFilter(DeviceOSFilter.ALL)
                    .status(Status.INVISIBLE)
                    .articleBookStoreList(null)
                    .deviceOSFilter(DeviceOSFilter.ALL)
                    .memberIdFilter(MemberIdFilter.ALL)
                    .view(1)
                    .build();

            articleRepository.save(article);
        }
    }

    public void dummyData3() {
        for (int i = 0; i < 15; i++) {
            Article article = articleRepository.findById((long) i + 1).orElseThrow();
            BookStore bookStore = bookStoreRepository.findById((long) i+1).orElseThrow();

            ArticleBookStore articleBookStore = articleBookStoreRepository.save(ArticleBookStore.builder()
                    .article(article)
                    .bookStore(bookStore)
                    .build());
            articleBookStoreRepository.save(articleBookStore);
        }
    }
}