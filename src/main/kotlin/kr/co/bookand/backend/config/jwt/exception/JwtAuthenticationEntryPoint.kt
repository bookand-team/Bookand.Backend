package kr.co.bookand.backend.config.jwt.exception

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.bookand.backend.common.KotlinErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException?
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "utf-8"
        response.status = HttpStatus.UNAUTHORIZED.value()

        val body = objectMapper.writeValueAsString(

            ApiErrorResponse(
                code = KotlinErrorCode.JWT_ERROR.errorCode,
                message = KotlinErrorCode.JWT_ERROR.errorMessage,
                log = KotlinErrorCode.JWT_ERROR.errorLog
            )
        )
        response.writer.write(body)
    }

}