package kr.co.bookand.backend.bookmark.domain

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.bookmark.domain.dto.KotlinBookmarkFolderRequest
import kr.co.bookand.backend.common.domain.KotlinBaseEntity
import javax.persistence.*

@Entity
class KotlinBookmark(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kbookmark_id")
    var id: Long = 0,

    var folderName: String,
    var folderImage: String? = null,

    @Enumerated(EnumType.STRING)
    var bookmarkType: KotlinBookmarkType,

    @ManyToOne(fetch = FetchType.LAZY)
    var account: KotlinAccount? = null,

    @OneToMany(mappedBy = "bookmark", cascade = [CascadeType.ALL], orphanRemoval = true)
    var bookmarkedArticleList: MutableList<KotlinBookmarkedArticle> = mutableListOf(),

    @OneToMany(mappedBy = "bookmark", cascade = [CascadeType.ALL], orphanRemoval = true)
    var bookmarkedBookstoreList: MutableList<KotlinBookmarkedBookstore> = mutableListOf()

) : KotlinBaseEntity() {

    constructor(account: KotlinAccount, kotlinBookmarkFolderRequest: KotlinBookmarkFolderRequest) : this(
        folderName = kotlinBookmarkFolderRequest.folderName,
        bookmarkType = KotlinBookmarkType.valueOf(kotlinBookmarkFolderRequest.bookmarkType),
        account = account
    )

    fun addBookmarkedArticle(bookmarkedArticle: KotlinBookmarkedArticle) {
        bookmarkedArticleList.add(bookmarkedArticle)
        bookmarkedArticle.bookmark = this
    }

    fun addBookmarkedBookstore(bookmarkedBookstore: KotlinBookmarkedBookstore) {
        bookmarkedBookstoreList.add(bookmarkedBookstore)
        bookmarkedBookstore.bookmark = this
    }

    fun updateFolderImage(folderImage: String) {
        this.folderImage = folderImage
    }

    fun updateBookmarkedBookStore(bookmarkedBookstoreList: MutableList<KotlinBookmarkedBookstore>) {
        this.bookmarkedBookstoreList = bookmarkedBookstoreList
    }

    fun updateBookmarkedArticle(bookmarkedArticleList: MutableList<KotlinBookmarkedArticle>) {
        this.bookmarkedArticleList = bookmarkedArticleList
    }

    fun updateFolderName(folderName: String) {
        this.folderName = folderName
    }

    fun deleteBookmarkedBookstore(bookmarkedBookstore: KotlinBookmarkedBookstore) {
        bookmarkedBookstoreList.remove(bookmarkedBookstore)
    }

    fun deleteBookmarkedArticle(bookmarkedArticle: KotlinBookmarkedArticle) {
        bookmarkedArticleList.remove(bookmarkedArticle)
    }
}