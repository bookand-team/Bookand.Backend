package kr.co.bookand.backend.config.jwt

import kr.co.bookand.backend.account.domain.KotlinAccount
import javax.persistence.*

@Entity
@Table(name = "krefresh_token")
class KotlinRefreshToken (
    @Id
    @Column(name = "rt_key")
    var key: String,

    @Column(name = "rt_value")
    var value: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    var account: KotlinAccount
){
    fun updateValue(value: String) {
        this.value = value
    }
}