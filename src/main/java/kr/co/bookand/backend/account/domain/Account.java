package kr.co.bookand.backend.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.bookand.backend.account.domain.dto.AccountDto;
import kr.co.bookand.backend.bookmark.BookMark;
import kr.co.bookand.backend.bookstore.domain.ReportBookStore;
import kr.co.bookand.backend.common.BaseTimeEntity;
import kr.co.bookand.backend.common.DeviceType;
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
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String password;
    private String provider;
    private String providerEmail;

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<BookMark> bookMarkList = new ArrayList<>();

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
}







