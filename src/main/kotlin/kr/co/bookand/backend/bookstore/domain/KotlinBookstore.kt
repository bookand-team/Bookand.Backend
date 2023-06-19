package kr.co.bookand.backend.bookstore.domain

import kr.co.bookand.backend.article.domain.KotlinIntroducedBookstore
import kr.co.bookand.backend.bookstore.domain.dto.KotlinBookstoreRequest
import kr.co.bookand.backend.common.KotlinStatus
import kr.co.bookand.backend.common.domain.KotlinBaseEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class KotlinBookstore(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kbookstore_id")
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
    var status: KotlinStatus,
    var view: Int,
    var bookmark: Int,
    var displayedAt: LocalDateTime?,

    @OneToMany(mappedBy = "bookStore", cascade = [CascadeType.ALL], orphanRemoval = true)
    var themeList: MutableList<KotlinBookstoreTheme> = mutableListOf(),

    @OneToMany(mappedBy = "bookStore", cascade = [CascadeType.ALL], orphanRemoval = true)
    var imageList: MutableList<KotlinBookstoreImage> = mutableListOf(),

    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL], orphanRemoval = true)
    var introducedBookstoreList: MutableList<KotlinIntroducedBookstore> = mutableListOf(),

    ) : KotlinBaseEntity() {
    constructor(kotlinBookstoreRequest : KotlinBookstoreRequest) : this(
        name = kotlinBookstoreRequest.name,
        address = kotlinBookstoreRequest.address,
        businessHours = kotlinBookstoreRequest.businessHours,
        contact = kotlinBookstoreRequest.contact,
        facility = kotlinBookstoreRequest.facility,
        sns = kotlinBookstoreRequest.sns,
        latitude = kotlinBookstoreRequest.latitude,
        longitude = kotlinBookstoreRequest.longitude,
        introduction = kotlinBookstoreRequest.introduction,
        mainImage = kotlinBookstoreRequest.mainImage,
        status = KotlinStatus.VISIBLE,
        view = 0,
        bookmark = 0,
        displayedAt = null
    )

    fun updateBookStoreStatus(status: KotlinStatus) {
        this.status = status
    }
    fun updateDisplayedAt(displayedAt: LocalDateTime) {
        this.displayedAt = displayedAt
    }

    fun updateBookStoreTheme(theme: KotlinBookstoreTheme) {
        themeList.add(theme)
    }

    fun updateBookStoreImage(image: KotlinBookstoreImage) {
        imageList.add(image)
    }

    fun updateIntroducedBookstore(kotlinIntroducedBookstore: KotlinIntroducedBookstore) {
        introducedBookstoreList.add(kotlinIntroducedBookstore)
    }

    fun updateBookStoreData(bookStoreRequest: KotlinBookstoreRequest) {
        name = bookStoreRequest.name
        address = bookStoreRequest.address
        businessHours = bookStoreRequest.businessHours
        contact = bookStoreRequest.contact
        facility = bookStoreRequest.facility
        sns = bookStoreRequest.sns
        introduction = bookStoreRequest.introduction
        mainImage = bookStoreRequest.mainImage
    }

    fun removeIntroducedBookstore(kotlinIntroducedBookstore: KotlinIntroducedBookstore) {
        introducedBookstoreList.remove(kotlinIntroducedBookstore)
    }

}