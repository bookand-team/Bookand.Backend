package kr.co.bookand.backend.issue.domain

import kr.co.bookand.backend.common.domain.KotlinBaseEntity
import javax.persistence.*

@Entity
class KotlinIssueImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kissue_image_id")
    var id: Long = 0,
    var imageUrl: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kissue_id")
    var issue: KotlinIssue

) : KotlinBaseEntity() {

    constructor(imageUrl: String, issue: KotlinIssue) : this(
        id = 0,
        imageUrl = imageUrl,
        issue = issue
    )
}