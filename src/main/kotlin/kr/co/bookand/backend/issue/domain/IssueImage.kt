package kr.co.bookand.backend.issue.domain

import kr.co.bookand.backend.common.domain.BaseEntity
import javax.persistence.*

@Entity
class IssueImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_image_id")
    var id: Long = 0,
    var imageUrl: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    var issue: Issue

) : BaseEntity() {

    constructor(imageUrl: String, issue: Issue) : this(
        id = 0,
        imageUrl = imageUrl,
        issue = issue
    )
}