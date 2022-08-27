package com.project.bookand.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.bookand.bookmark.BookMark_Article;
import com.project.bookand.bookmark.BookMark_BookStore;
import com.project.bookand.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    private String mainPicture;

    private Integer hit;

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<BookMark_Article> mark_articleList = new ArrayList<>();

}
