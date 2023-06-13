package kr.co.bookand.backend.common

import org.springframework.data.domain.Page

data class KotlinPageResponse<T>(
    val totalPages: Int,
    val totalElements: Long,
    val last: Boolean,
    val content: List<T>
) {
    companion object {
        fun <T> of(page: Page<T>): KotlinPageResponse<T> {
            return KotlinPageResponse(
                totalPages = page.totalPages,
                totalElements = page.totalElements,
                last = page.isLast,
                content = page.content
            )
        }

        fun <T> ofCursor(page: Page<T>, totalElements: Long): KotlinPageResponse<T> {
            return KotlinPageResponse(
                totalPages = 0,
                totalElements = totalElements,
                last = page.isLast,
                content = page.content
            )
        }

        fun getTotalElements(totalElements: Long, cursorElements: Long?): Long {
            var updatedTotalElements = totalElements
            cursorElements?.let {
                updatedTotalElements += it
            }
            return updatedTotalElements.coerceAtLeast(0)
        }
    }
}