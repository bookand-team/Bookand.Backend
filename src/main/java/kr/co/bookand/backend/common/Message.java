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
//    private CodeStatus status;
    private String msg;

//    public static Message of(CodeStatus status, String msg) {
//        return Message.builder()
//                .status(status)
//                .msg(msg)
//                .build();
//    }
//
//    public static Message of(String msg) {
//        return Message.of(CodeStatus.FAIL, msg);
//    }

    public static Message of(String msg) {
        return Message.builder()
                .msg(msg)
                .build();
    }
}
