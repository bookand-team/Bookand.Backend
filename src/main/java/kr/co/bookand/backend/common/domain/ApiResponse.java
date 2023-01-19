package kr.co.bookand.backend.common.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    // 유효성 검사 실패 -> BindException 에 BindingResult 값이 담겨 있음
    public static ApiResponse<?> fail(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ApiResponse<>(errors);
    }

    // 예외 발생
    public static ApiResponse<?> error(Message message) {
        return new ApiResponse<>(message);
    }

    private ApiResponse(T data) {
        this.data = data;
    }
}
