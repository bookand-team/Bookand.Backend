package kr.co.bookand.backend.account.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class KotlinAccount(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kaccount_id")
    var id: Long = 0,

    var email: String,
    var password: String,
    var nickname: String,
    var provider: String,
    var providerEmail: String,
    var profileImage: String,
    var lastLoginDate: LocalDateTime,
    var signUpDate : LocalDateTime,
    @Enumerated(EnumType.STRING)
    var role : Role,
    @Enumerated(EnumType.STRING)
    var accountStatus : AccountStatus

) {
    fun updateProfile(profileImage: String, nickname: String) {
        this.profileImage = profileImage
        this.nickname = nickname
    }

}