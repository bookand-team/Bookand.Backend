package kr.co.bookand.backend.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

public record Message (
     String result,
     @JsonIgnore
     int statusCode
){
    @Builder
    public Message {
    }

    public static Message of(String msg, int statusCode) {
        return Message.builder()
                .result(msg)
                .statusCode(statusCode)
                .build();
    }

    public static Message of(String msg) {
        return Message.builder()
                .result(msg)
                .build();
    }
}
