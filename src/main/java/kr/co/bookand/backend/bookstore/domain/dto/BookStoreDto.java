package kr.co.bookand.backend.bookstore.domain.dto;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.Theme;
import kr.co.bookand.backend.common.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BookStoreDto {
    private Long id;
    private String name;
    private String location;
    private String hours;
    private String number;
    private String concept;
    private String image;
    private String sns;
    private String facility;
    private String story;
    private String theme;
    private String status;

    public static BookStoreDto of(BookStore bookStore) {
        return BookStoreDto.builder()
                .id(bookStore.getId())
                .name(bookStore.getName())
                .location(bookStore.getLocation())
                .hours(bookStore.getHours())
                .number(bookStore.getNumber())
                .concept(bookStore.getConcept())
                .sns(bookStore.getSns())
                .facility(bookStore.getFacility())
                .story(bookStore.getStory())
                .theme(bookStore.getTheme().name())
                .status(bookStore.getStatus().name())
                .build();
    }

    public BookStore toBookStore() {
        return BookStore.builder()
                .id(id)
                .name(name)
                .location(location)
                .hours(hours)
                .number(number)
                .concept(concept)
                .sns(sns)
                .facility(facility)
                .story(story)
                .theme(Theme.valueOf(theme))
                .status(Status.INVISIBLE)
                .build();
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookStoreSimpleDto{
        private Long bookStoreId;
    }
}
