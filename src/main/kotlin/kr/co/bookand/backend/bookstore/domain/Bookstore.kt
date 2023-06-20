package kr.co.bookand.backend.bookstore.domain

import kr.co.bookand.backend.article.domain.IntroducedBookstore
import kr.co.bookand.backend.bookstore.domain.dto.BookstoreRequest
import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.common.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Bookstore(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookstore_id")
    var id: Long = 0,
    var name: String,
    var address: String,
    var businessHours: String,
    var contact: String,
    var facility: String,
    var sns: String,

    var latitude: String,
    var longitude: String,

    var introduction: String,
    var mainImage: String,
    @Enumerated(EnumType.STRING)
    var status: Status,
    var view: Int,
    var bookmark: Int,
    var displayedAt: LocalDateTime?,

    @OneToMany(mappedBy = "bookstore", cascade = [CascadeType.ALL], orphanRemoval = true)
    var themeList: MutableList<BookstoreTheme> = mutableListOf(),

    @OneToMany(mappedBy = "bookstore", cascade = [CascadeType.ALL], orphanRemoval = true)
    var imageList: MutableList<BookstoreImage> = mutableListOf(),

    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL], orphanRemoval = true)
    var introducedBookstoreList: MutableList<IntroducedBookstore> = mutableListOf(),

    ) : BaseEntity() {
    constructor(bookstoreRequest : BookstoreRequest) : this(
        name = bookstoreRequest.name,
        address = bookstoreRequest.address,
        businessHours = bookstoreRequest.businessHours,
        contact = bookstoreRequest.contact,
        facility = bookstoreRequest.facility,
        sns = bookstoreRequest.sns,
        latitude = bookstoreRequest.latitude,
        longitude = bookstoreRequest.longitude,
        introduction = bookstoreRequest.introduction,
        mainImage = bookstoreRequest.mainImage,
        status = Status.VISIBLE,
        view = 0,
        bookmark = 0,
        displayedAt = null
    )

    fun updateBookstoreStatus(status: Status) {
        this.status = status
    }
    fun updateDisplayedAt(displayedAt: LocalDateTime) {
        this.displayedAt = displayedAt
    }

    fun updateBookstoreTheme(theme: BookstoreTheme) {
        themeList.add(theme)
    }

    fun updateBookstoreThemeList(themeList: MutableList<BookstoreTheme>) {
        this.themeList = themeList
    }

    fun updateBookstoreImage(image: BookstoreImage) {
        imageList.add(image)
    }

    fun updateBookstoreImageList(imageList: MutableList<BookstoreImage>) {
        this.imageList = imageList
    }

    fun updateIntroducedBookstore(introducedBookstore: IntroducedBookstore) {
        introducedBookstoreList.add(introducedBookstore)
    }

    fun updateBookstoreData(bookstoreRequest: BookstoreRequest) {
        name = bookstoreRequest.name
        address = bookstoreRequest.address
        businessHours = bookstoreRequest.businessHours
        contact = bookstoreRequest.contact
        facility = bookstoreRequest.facility
        sns = bookstoreRequest.sns
        introduction = bookstoreRequest.introduction
        mainImage = bookstoreRequest.mainImage
    }

    fun removeIntroducedBookstore(introducedBookstore: IntroducedBookstore) {
        introducedBookstoreList.remove(introducedBookstore)
    }

}