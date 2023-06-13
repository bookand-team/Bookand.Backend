package kr.co.bookand.backend.account.domain

import kr.co.bookand.backend.common.domain.KotlinBaseEntity
import javax.persistence.*

@Entity
class KotlinRevokeAccount(
    @Id
    @GeneratedValue
    @Column(name = "krevoke_account_id")
    var id: Long? = 0,

    var reason: String,

    @Enumerated(EnumType.STRING)
    var revokeType: KotlinRevokeType,

    var accountId: Long
): KotlinBaseEntity() {
}