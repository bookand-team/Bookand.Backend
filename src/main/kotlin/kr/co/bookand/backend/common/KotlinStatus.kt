package kr.co.bookand.backend.common


enum class KotlinStatus {
    INVISIBLE, VISIBLE, REMOVE;

    companion object {
        fun toEnum(status: String): KotlinStatus {
            return when (status) {
                INVISIBLE.name -> INVISIBLE
                VISIBLE.name -> VISIBLE
                REMOVE.name -> REMOVE
                else -> throw IllegalArgumentException("잘못된 상태값입니다 : $status")
            }
        }
    }
}
