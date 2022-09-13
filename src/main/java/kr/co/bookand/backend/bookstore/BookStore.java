package kr.co.bookand.backend.bookstore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.bookand.backend.article.Article;
import kr.co.bookand.backend.common.BaseTimeEntity;
import kr.co.bookand.backend.bookmark.BookMarkBookStore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private String Story;

    @Enumerated(EnumType.STRING)
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @JsonIgnore
    @OneToMany(mappedBy = "bookStore", cascade = CascadeType.ALL)
    private List<BookMarkBookStore> mark_bookStoreList = new ArrayList<>();
}
