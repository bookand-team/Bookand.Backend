package kr.co.bookand.backend.account.model

import kr.co.bookand.backend.bookmark.model.Bookmark
import kr.co.bookand.backend.bookstore.model.ReportBookstore
import kr.co.bookand.backend.common.model.BaseEntity
import kr.co.bookand.backend.feedback.model.Feedback
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    var id: Long = 0,

    var email: String,
    var password: String,
    var nickname: String,
    var provider: String,
    var providerEmail: String,
    var profileImage: String? = null,
    var lastLoginDate: LocalDateTime? = null,
    var signUpDate: LocalDateTime? = null,

    @Enumerated(EnumType.STRING)
    var role: Role,
    @Enumerated(EnumType.STRING)
    var accountStatus: AccountStatus,

    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], orphanRemoval = true)
    val feedbackList: MutableList<Feedback> = mutableListOf(),

    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], orphanRemoval = true)
    var bookmarkList: MutableList<Bookmark> = mutableListOf(),

    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], orphanRemoval = true)
    var tipList: MutableList<ReportBookstore> = mutableListOf(),

    ) : BaseEntity() {
    fun updateProfile(profileImage: String, nickname: String) {
        this.profileImage = profileImage
        this.nickname = nickname
    }

    fun updateLastLoginDate() {
        this.lastLoginDate = LocalDateTime.now()
    }

    fun updateAccountStatus(accountStatus: AccountStatus) {
        this.accountStatus = accountStatus
    }

    fun updateBookmarks(bookmarkList: MutableList<Bookmark>) {
        this.bookmarkList = bookmarkList
    }

    fun deletedBanAccount() {
        this.nickname = "BAN"
        this.password = "BAN"
        this.email = "BAN"
        this.provider = "BAN"
        this.providerEmail = "BAN"
        this.profileImage = "BAN"
        this.role = Role.SUSPENDED
    }

}