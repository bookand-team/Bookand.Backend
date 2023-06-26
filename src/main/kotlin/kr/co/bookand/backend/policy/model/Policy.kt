package kr.co.bookand.backend.policy.model

import kr.co.bookand.backend.common.model.BaseEntity
import javax.persistence.*

@Entity
class Policy(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    var id: Long = 0,
    var name: String,
    var title: String,
    @Column(columnDefinition = "text", length = 30000)
    var content: String
) : BaseEntity() {
    fun updateContent(content: String) {
        this.content = content
    }
}

