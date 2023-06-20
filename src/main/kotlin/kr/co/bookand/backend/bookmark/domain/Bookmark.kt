package kr.co.bookand.backend.bookmark.domain

import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.bookmark.domain.dto.BookmarkFolderRequest
import kr.co.bookand.backend.common.domain.BaseEntity
import javax.persistence.*

@Entity
class Bookmark(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    var id: Long = 0,

    var folderName: String,
    var folderImage: String? = null,

    @Enumerated(EnumType.STRING)
    var bookmarkType: BookmarkType,

    @ManyToOne(fetch = FetchType.LAZY)
    var account: Account? = null,

    @OneToMany(mappedBy = "bookmark", cascade = [CascadeType.ALL], orphanRemoval = true)
    var bookmarkedArticleList: MutableList<BookmarkedArticle> = mutableListOf(),

    @OneToMany(mappedBy = "bookmark", cascade = [CascadeType.ALL], orphanRemoval = true)
    var bookmarkedBookstoreList: MutableList<BookmarkedBookstore> = mutableListOf()

) : BaseEntity() {

    constructor(account: Account, bookmarkFolderRequest: BookmarkFolderRequest) : this(
        folderName = bookmarkFolderRequest.folderName,
        bookmarkType = BookmarkType.valueOf(bookmarkFolderRequest.bookmarkType),
        account = account
    )

    fun addBookmarkedArticle(bookmarkedArticle: BookmarkedArticle) {
        bookmarkedArticleList.add(bookmarkedArticle)
        bookmarkedArticle.bookmark = this
    }

    fun addBookmarkedBookstore(bookmarkedBookstore: BookmarkedBookstore) {
        bookmarkedBookstoreList.add(bookmarkedBookstore)
        bookmarkedBookstore.bookmark = this
    }

    fun updateFolderImage(folderImage: String) {
        this.folderImage = folderImage
    }

    fun updateBookmarkedBookStore(bookmarkedBookstoreList: MutableList<BookmarkedBookstore>) {
        this.bookmarkedBookstoreList = bookmarkedBookstoreList
    }

    fun updateBookmarkedArticle(bookmarkedArticleList: MutableList<BookmarkedArticle>) {
        this.bookmarkedArticleList = bookmarkedArticleList
    }

    fun updateFolderName(folderName: String) {
        this.folderName = folderName
    }

    fun deleteBookmarkedBookstore(bookmarkedBookstore: BookmarkedBookstore) {
        bookmarkedBookstoreList.remove(bookmarkedBookstore)
    }

    fun deleteBookmarkedArticle(bookmarkedArticle: BookmarkedArticle) {
        bookmarkedArticleList.remove(bookmarkedArticle)
    }
}