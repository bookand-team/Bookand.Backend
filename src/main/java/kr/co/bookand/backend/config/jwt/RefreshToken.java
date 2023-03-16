package kr.co.bookand.backend.config.jwt;

import kr.co.bookand.backend.account.domain.Account;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "refresh_token")
@Entity
@Builder
public class RefreshToken {

    @Id
    @Column(name = "rt_key")
    private String key;

    @Column(name = "rt_value")
    private String value;

    @OneToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private Account account;
}