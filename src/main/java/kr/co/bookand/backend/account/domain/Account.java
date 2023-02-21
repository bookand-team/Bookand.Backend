package kr.co.bookand.backend.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.bookand.backend.account.domain.dto.AccountDto;

import kr.co.bookand.backend.bookmark.domain.Bookmark;
import kr.co.bookand.backend.bookstore.domain.ReportBookStore;
import kr.co.bookand.backend.common.domain.BaseEntity;
import kr.co.bookand.backend.notification.Notification;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    private String email;
    private String nickname;
    private String password;
    private String provider;
    private String providerEmail;
    private String profileImage;

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

    public AccountDto.MemberRequest toAccountRequestDto(String suffix) {
        return new AccountDto.MemberRequest(email, email + suffix, nickname, provider);
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }

    public void updateBookmarkList(List<Bookmark> bookmark) {
        if (bookmarkList == null) {
            bookmarkList = new ArrayList<>();
        }
        bookmarkList.addAll(bookmark);
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}