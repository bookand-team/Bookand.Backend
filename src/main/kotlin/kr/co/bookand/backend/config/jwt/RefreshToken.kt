package kr.co.bookand.backend.config.jwt

import kr.co.bookand.backend.account.model.Account
import javax.persistence.*

@Entity
@Table(name = "refresh_token")
class RefreshToken (
    @Id
    @Column(name = "rt_key")
    var key: String,

    @Column(name = "rt_value")
    var value: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    var account: Account
){
    fun updateValue(value: String) {
        this.value = value
    }
}