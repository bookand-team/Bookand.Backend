package com.project.bookand.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.bookand.bookmark.BookMark_BookStore;
import com.project.bookand.bookmark.Bookmark;
import com.project.bookand.bookstore.Tip;
import com.project.bookand.common.BaseTimeEntity;
import com.project.bookand.notification.Notification;
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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String password;

    private String provider;
    private String provider_id;
    private String provider_name;
    private String profile;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Notification> notificationList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Tip> tipList = new ArrayList<>();

}







