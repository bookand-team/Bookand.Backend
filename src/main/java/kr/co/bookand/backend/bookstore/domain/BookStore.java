package kr.co.bookand.backend.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.common.domain.BaseTimeEntity;
import kr.co.bookand.backend.bookmark.BookMarkBookStore;
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
public class BookStore extends BaseTimeEntity {

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
    private BookstoreStatus status;

    private int view;
    private int bookmark;

    @Enumerated(EnumType.STRING)
    private BookstoreTheme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @JsonIgnore
    @OneToMany(mappedBy = "bookStore", cascade = CascadeType.ALL)
    private List<BookMarkBookStore> mark_bookStoreList = new ArrayList<>();

    public void updateBookStoreData(BookStoreRequest bookStoreRequest) {
        this.name = bookStoreRequest.name();
        this.address = bookStoreRequest.address();
        this.businessHours = bookStoreRequest.businessHours();
        this.contact = bookStoreRequest.contact();
        this.facility = bookStoreRequest.facility();
        this.sns = bookStoreRequest.sns();
        this.introduction = bookStoreRequest.introduction();
        this.mainImage = bookStoreRequest.mainImage();
        this.status = BookstoreStatus.valueOf(bookStoreRequest.status());
        this.theme = BookstoreTheme.valueOf(bookStoreRequest.theme());
    }

    public void updateBookStoreStatus(BookstoreStatus status) {
        this.status = status;
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

    public void updateBookStoreSubImage(BookStoreResponse bookStoreDto) {
        bookStoreDto.subImage().forEach(subImage -> {
            BookStoreImage bookStoreImage = BookStoreImage.builder()
                    .bookStore(this)
                    .url(subImage)
                    .build();
            this.subImages.add(bookStoreImage);
        });
    }

    public void updateBookStoreSubImageDelete(String url) {
        this.subImages.removeIf(subImage -> subImage.getUrl().equals(url));
    }

}
