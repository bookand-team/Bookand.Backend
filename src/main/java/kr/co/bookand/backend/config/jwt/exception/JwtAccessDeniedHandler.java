package kr.co.bookand.backend.config.jwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.bookand.backend.common.exception.ApiErrorResponse;
import kr.co.bookand.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        String body = objectMapper.writeValueAsString(
                ApiErrorResponse.builder().code(ErrorCode.NOT_ROLE_MEMBER.getErrorCode()).message(ErrorCode.NOT_ROLE_MEMBER.getErrorLog()).build());
        response.getWriter().write(body);

    }
}