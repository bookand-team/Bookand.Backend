package kr.co.bookand.backend.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private String result;

    public static Message of(String msg) {
        return Message.builder()
                .result(msg)
                .build();
    }
}
