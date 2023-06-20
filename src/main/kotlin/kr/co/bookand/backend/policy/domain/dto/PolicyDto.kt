package kr.co.bookand.backend.policy.domain.dto

import kr.co.bookand.backend.policy.domain.Policy

data class CreatePolicyRequest(
    val policyId: Long,
    val title: String,
    val name: String,
    val content: String
)

data class PolicyResponse(
    val policyId: Long,
    val title: String,
    val name: String,
    val content: String
) {
    constructor(policy: Policy) : this(
        policyId = policy.id,
        title = policy.title,
        name = policy.name,
        content = policy.content
    )
}

data class PolicyIdResponse(
    val id: Long
)