package kr.co.bookand.backend.account.exception;

import lombok.Getter;

@Getter
public class NotFoundUserInformationException extends RuntimeException {
    public NotFoundUserInformationException() {
        super("유저 정보를 찾을 수 없습니다");
    }
}