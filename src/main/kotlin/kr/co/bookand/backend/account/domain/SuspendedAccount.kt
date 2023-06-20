package kr.co.bookand.backend.account.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class SuspendedAccount(
    @Id
    @GeneratedValue
    @Column(name = "suspended_account_id")
    var id: Long? = 0,

    var suspendedCount: Int = 0,
    var startedSuspendedAt: LocalDateTime? = null,
    var endedSuspendedAt: LocalDateTime? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    var account: Account
) {

    fun addSuspendedCount() {
        suspendedCount++
    }

    fun setSuspendedAt(suspendedDate: LocalDateTime) {
        this.startedSuspendedAt = LocalDateTime.now()
        this.endedSuspendedAt = suspendedDate
    }
}