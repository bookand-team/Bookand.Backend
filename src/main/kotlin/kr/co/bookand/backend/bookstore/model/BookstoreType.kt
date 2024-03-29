package kr.co.bookand.backend.bookstore.model

enum class BookstoreType {
    // 여행, 음악, 그림, 애완동물, 영화, 추리, 역사
    TRAVEL, MUSIC, PICTURE, PET, MOVIE, DETECTIVE, HISTORY, NONE;

    companion object {
        fun randomEnum(): List<BookstoreType> {
            // 중복되지 않게 랜덤 출력
            val hashSet = HashSet<Int>()
            val bookStoreTypes: MutableList<BookstoreType> = ArrayList()
            while (hashSet.size < 3) {
                hashSet.add((Math.random() * 7).toInt())
            }
            for (luckyNum in hashSet) {
                bookStoreTypes.add(BookstoreType.values()[luckyNum])
            }
            return bookStoreTypes
        }
    }
}