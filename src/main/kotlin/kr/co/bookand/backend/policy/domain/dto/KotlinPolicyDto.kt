package kr.co.bookand.backend.policy.domain.dto

import kr.co.bookand.backend.policy.domain.KotlinPolicy

data class KotlinPolicyRequest(
    val policyId: Long,
    val title: String,
    val name: String,
    val content: String
)

data class KotlinPolicyResponse(
    val policyId: Long,
    val title: String,
    val name: String,
    val content: String
) {
    constructor(kotlinPolicy: KotlinPolicy) : this(
        policyId = kotlinPolicy.id,
        title = kotlinPolicy.title,
        name = kotlinPolicy.name,
        content = kotlinPolicy.content
    )
}
