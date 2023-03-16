package kr.co.bookand.backend.account.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SuspendedAccount {

    @Id
    @GeneratedValue
    @Column(name = "revoke_account_id")
    private Long id;

    private int suspendedCount;
    private LocalDateTime startedSuspendedDate;
    private LocalDateTime endedSuspendedDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private Account account;

    public void addSuspendedCount() {
        this.suspendedCount++;
    }

    public void setSuspendedDate(LocalDateTime suspendedDate) {
        this.startedSuspendedDate = LocalDateTime.now();
        this.endedSuspendedDate = suspendedDate;
    }

}
