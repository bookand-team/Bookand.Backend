package kr.co.bookand.backend.account.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class KotlinSuspendedAccount(
    @Id
    @GeneratedValue
    @Column(name = "krevoke_account_id")
    var id: Long? = 0,

    var suspendedCount: Int = 0,
    var startedSuspendedAt: LocalDateTime? = null,
    var endedSuspendedAt: LocalDateTime? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kaccount_id")
    var account: KotlinAccount
) {

    fun addSuspendedCount() {
        suspendedCount++
    }

    fun setSuspendedAt(suspendedDate: LocalDateTime) {
        this.startedSuspendedAt = LocalDateTime.now()
        this.endedSuspendedAt = suspendedDate
    }
}