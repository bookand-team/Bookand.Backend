package kr.co.bookand.backend.account.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RevokeAccount {

    @Id
    @GeneratedValue
    @Column(name = "revoke_account_id")
    private Long id;

    private String reason;

    @Enumerated(EnumType.STRING)
    private RevokeType revokeType;

    private Long accountId;
}
