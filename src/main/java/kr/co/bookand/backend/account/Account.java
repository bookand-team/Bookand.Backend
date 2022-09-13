package kr.co.bookand.backend.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.bookand.backend.bookmark.Bookmark;
import kr.co.bookand.backend.bookstore.ReportBookStore;
import kr.co.bookand.backend.common.BaseTimeEntity;
import kr.co.bookand.backend.notification.Notification;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private List<ReportBookStore> tipList = new ArrayList<>();

}







