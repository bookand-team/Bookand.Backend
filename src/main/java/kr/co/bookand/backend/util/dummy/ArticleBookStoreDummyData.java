package kr.co.bookand.backend.util.dummy;


import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.ArticleBookStore;
import kr.co.bookand.backend.article.domain.ArticleCategory;
import kr.co.bookand.backend.article.domain.ArticleTag;
import kr.co.bookand.backend.article.repository.ArticleBookStoreRepository;
import kr.co.bookand.backend.article.repository.ArticleRepository;
import kr.co.bookand.backend.article.repository.ArticleTagRepository;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.BookStoreImage;
import kr.co.bookand.backend.bookstore.domain.BookStoreTheme;
import kr.co.bookand.backend.bookstore.domain.BookStoreType;
import kr.co.bookand.backend.bookstore.repository.BookStoreImageRepository;
import kr.co.bookand.backend.bookstore.repository.BookStoreRepository;
import kr.co.bookand.backend.bookstore.repository.BookStoreThemeRepository;
import kr.co.bookand.backend.common.domain.DeviceOSFilter;
import kr.co.bookand.backend.common.domain.MemberIdFilter;
import kr.co.bookand.backend.common.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@RequiredArgsConstructor
public class ArticleBookStoreDummyData {

    private final ArticleBookStoreRepository articleBookStoreRepository;
    private final ArticleRepository articleRepository;
    private final ArticleTagRepository articleTagRepository;
    private final BookStoreRepository bookStoreRepository;
    private final BookStoreImageRepository bookStoreImageRepository;
    private final BookStoreThemeRepository bookStoreThemeRepository;

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
                    .mainImage("mainImage%d".formatted(i))
                    .introduction("introduction%d".formatted(i))
                    .status(Status.valueOf("VISIBLE"))
                    .displayDate(LocalDateTime.now())
                    .view(1)
                    .build();
            BookStore save = bookStoreRepository.save(store);
            save.updateBookStoreSubImage(bookStoreImageDummyData(save));
            save.updateBookStoreTheme(bookStoreThemeDummyData(save));

        }

    }

    public void dummyData2() {
        for (int i = 0; i < 15; i++) {
            Article article = Article.builder()
                    .title("title%d".formatted(i))
                    .content("content%d".formatted(i))
                    .writer("writer%d".formatted(i))
                    .articleTagList(null)
                    .category(ArticleCategory.BOOK_REVIEW)
                    .memberIdFilter(MemberIdFilter.ALL)
                    .deviceOSFilter(DeviceOSFilter.ALL)
                    .status(Status.VISIBLE)
                    .articleBookStoreList(null)
                    .mainImage("mainImage%d".formatted(i))
                    .deviceOSFilter(DeviceOSFilter.ALL)
                    .memberIdFilter(MemberIdFilter.ALL)
                    .displayDate(LocalDateTime.now())
                    .view(1)
                    .build();

            Article save = articleRepository.save(article);
            save.updateArticleTagList(articleTagDummyData(article));
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

    public List<ArticleTag> articleTagDummyData(Article article) {
        List<ArticleTag> articleTagList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ArticleTag articleTag = ArticleTag.builder()
                    .tag("name%d".formatted(i))
                    .article(article)
                    .build();
            articleTagRepository.save(articleTag);
            articleTagList.add(articleTag);
        }
        return articleTagList;
    }

    public List<BookStoreImage> bookStoreImageDummyData(BookStore bookStore) {
        List<BookStoreImage> bookStoreImageList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            BookStoreImage bookStoreImage = BookStoreImage.builder()
                    .url("image%d".formatted(i))
                    .bookStore(bookStore)
                    .build();
            bookStoreImageRepository.save(bookStoreImage);
            bookStoreImageList.add(bookStoreImage);
        }
        return bookStoreImageList;
    }

    public List<BookStoreTheme> bookStoreThemeDummyData(BookStore bookStore) {
        List<BookStoreTheme> bookStoreThemeList = new ArrayList<>();
        List<BookStoreType> bookStoreTypes = BookStoreType.randomEnum();
        for (int i = 0; i < 3; i++) {
            BookStoreTheme bookStoreTheme = BookStoreTheme.builder()
                    .theme(bookStoreTypes.get(i))
                    .bookStore(bookStore)
                    .build();
            bookStoreThemeRepository.save(bookStoreTheme);
            bookStoreThemeList.add(bookStoreTheme);
        }
        return bookStoreThemeList;
    }
}
