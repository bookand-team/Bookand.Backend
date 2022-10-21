package kr.co.bookand.backend.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto;
import kr.co.bookand.backend.common.BaseTimeEntity;
import kr.co.bookand.backend.bookmark.BookMarkBookStore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private String location;
    private String hours;
    private String number;
    private String concept;
    private String image;
    private String sns;
    private String facility;
    private String story;

    @Enumerated(EnumType.STRING)
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @JsonIgnore
    @OneToMany(mappedBy = "bookStore", cascade = CascadeType.ALL)
    private List<BookMarkBookStore> mark_bookStoreList = new ArrayList<>();

    public void updateBookStoreData(BookStoreDto bookStoreDto) {
        this.name = bookStoreDto.getName();
        this.location = bookStoreDto.getLocation();
        this.hours = bookStoreDto.getHours();
        this.number = bookStoreDto.getNumber();
        this.concept = bookStoreDto.getConcept();
        this.image = bookStoreDto.getImage();
        this.sns = bookStoreDto.getSns();
        this.facility = bookStoreDto.getFacility();
        this.story = bookStoreDto.getStory();
        this.theme = Theme.valueOf(bookStoreDto.getTheme());
    }
}
