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
    private Long id;

    private String email;
    private String nickname;
    private String password;
    private String provider;
    private String providerEmail;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isSign = false;

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

    public void updateIsSign() {
        this.isSign = !isSign;
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }
}