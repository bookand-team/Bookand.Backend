package kr.co.bookand.backend.account.domain

import kr.co.bookand.backend.common.domain.BaseEntity
import javax.persistence.*

@Entity
class RevokeAccount(
    @Id
    @GeneratedValue
    @Column(name = "revoke_account_id")
    var id: Long? = 0,

    var reason: String,

    @Enumerated(EnumType.STRING)
    var revokeType: RevokeType,

    var accountId: Long
): BaseEntity() {
}