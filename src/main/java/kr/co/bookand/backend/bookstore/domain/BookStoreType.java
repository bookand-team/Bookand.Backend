package kr.co.bookand.backend.bookstore.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public enum BookStoreType {
    // 여행, 음악, 그림, 애완동물, 영화, 추리, 역사
    TRAVEL, MUSIC, PICTURE, PET, MOVIE, DETECTIVE, HISTORY;

    public static List<BookStoreType> randomEnum() {
        // 중복되지 않게 랜덤 출력
        HashSet<Integer> hashSet = new HashSet<>();
        List<BookStoreType> bookStoreTypes = new ArrayList<>();
        while (hashSet.size() < 3) {
            hashSet.add((int) (Math.random() * 7));
        }
        for(int luckyNum : hashSet){
            bookStoreTypes.add(BookStoreType.values()[luckyNum]);
        }
        return bookStoreTypes;
    }
}
