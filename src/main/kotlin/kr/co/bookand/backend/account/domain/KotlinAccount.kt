package kr.co.bookand.backend.account.domain

import kr.co.bookand.backend.bookmark.domain.KotlinBookmark
import kr.co.bookand.backend.bookstore.domain.KotlinReportBookstore
import kr.co.bookand.backend.common.domain.KotlinBaseEntity
import kr.co.bookand.backend.feedback.domain.KotlinFeedback
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
    var signUpDate: LocalDateTime,

    @Enumerated(EnumType.STRING)
    var role: KotlinRole,
    @Enumerated(EnumType.STRING)
    var accountStatus: KotlinAccountStatus,

    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], orphanRemoval = true)
    val feedbackList: MutableList<KotlinFeedback> = mutableListOf(),

    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], orphanRemoval = true)
    var bookmarkList: MutableList<KotlinBookmark> = mutableListOf(),

    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], orphanRemoval = true)
    var tipList: MutableList<KotlinReportBookstore> = mutableListOf(),

    ): KotlinBaseEntity() {
    fun updateProfile(profileImage: String, nickname: String) {
        this.profileImage = profileImage
        this.nickname = nickname
    }

}