package kr.co.bookand.backend.config.jwt.exception;


import kr.co.bookand.backend.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JavaExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JavaJwtException ex) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ex.getErrorCode());
        } catch (RuntimeException ex) {
            log.error("runtime exception exception handler filter");
            setErrorResponse(HttpStatus.BAD_REQUEST, response, ErrorCode.INTER_SERVER_ERROR);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, ErrorCode ex) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        JavaJwtException errorResponse = new JavaJwtException(ex, ex.getErrorLog());
        try {
            String json = errorResponse.convertToJson(ex.getErrorLog());
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
