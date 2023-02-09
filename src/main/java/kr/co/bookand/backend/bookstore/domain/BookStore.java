package kr.co.bookand.backend.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.bookand.backend.article.domain.ArticleBookStore;
import kr.co.bookand.backend.common.domain.BaseEntity;
import kr.co.bookand.backend.bookmark.BookMarkBookStore;
import kr.co.bookand.backend.common.domain.Status;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookStore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String businessHours;
    private String contact;
    private String facility;
    private String sns;

    private String introduction;
    private String mainImage;

    @OneToMany(mappedBy = "bookStore", cascade = CascadeType.ALL)
    private List<BookStoreImage> subImages = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    private int view;
    private int bookmark;

    @Enumerated(EnumType.STRING)
    private BookstoreTheme theme;

    @OneToMany(mappedBy = "bookStore")
    private List<ArticleBookStore> articleBookStoreList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "bookStore")
    private List<BookMarkBookStore> bookmarkBookStoreList = new ArrayList<>();

    public void updateBookStoreData(BookStoreRequest bookStoreRequest) {
        this.name = bookStoreRequest.name();
        this.address = bookStoreRequest.address();
        this.businessHours = bookStoreRequest.businessHours();
        this.contact = bookStoreRequest.contact();
        this.facility = bookStoreRequest.facility();
        this.sns = bookStoreRequest.sns();
        this.introduction = bookStoreRequest.introduction();
        this.mainImage = bookStoreRequest.mainImage();
        this.status = Status.valueOf(bookStoreRequest.status());
        this.theme = BookstoreTheme.valueOf(bookStoreRequest.theme());
    }

    public void updateBookStoreStatus(Status status) {
        this.status = status;
    }

    public void updateArticleBookStore(ArticleBookStore articleBookStore) {
        if (articleBookStoreList == null) {
            articleBookStoreList = new ArrayList<>();
        }
        this.articleBookStoreList.add(articleBookStore);
    }

    public void removeArticleBookStore(ArticleBookStore articleBookStore) {
        this.articleBookStoreList.remove(articleBookStore);
    }

    public void updateBookStoreView() {
        this.view++;
    }

    public void updateBookStoreBookmark() {
        this.bookmark++;
    }

    public void updateBookStoreUnBookmark() {
        this.bookmark--;
    }

    public void updateBookStoreImage(BookStoreResponse bookStoreDto) {
        this.mainImage = bookStoreDto.mainImage();
    }

    public void updateBookStoreSubImageDelete(String url) {
        this.subImages.removeIf(subImage -> subImage.getUrl().equals(url));
    }

}
