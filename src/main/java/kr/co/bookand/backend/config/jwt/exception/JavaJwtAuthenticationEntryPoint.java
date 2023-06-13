package kr.co.bookand.backend.config.jwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.bookand.backend.common.exception.ApiErrorResponse;
import kr.co.bookand.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JavaJwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.warn("Unauthorized access = {}", authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        String body = objectMapper.writeValueAsString(
                ApiErrorResponse.builder()
                        .code(ErrorCode.JWT_ERROR.getErrorCode())
                        .message(ErrorCode.JWT_ERROR.getErrorMessage())
                        .log(ErrorCode.JWT_ERROR.getErrorLog())
                        .build());
        response.getWriter().write(body);

    }
}