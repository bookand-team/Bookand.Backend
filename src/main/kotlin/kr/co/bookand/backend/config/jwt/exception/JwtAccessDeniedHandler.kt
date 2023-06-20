package kr.co.bookand.backend.config.jwt.exception

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.bookand.backend.common.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException?
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "utf-8"
        response.status = HttpStatus.FORBIDDEN.value()

        val body = objectMapper.writeValueAsString(
            ApiErrorResponse(
                code = ErrorCode.NOT_ROLE_MEMBER.errorCode,
                message = ErrorCode.NOT_ROLE_MEMBER.errorMessage,
                log = ErrorCode.NOT_ROLE_MEMBER.errorLog
            )
        )
        response.writer.write(body)
    }
}
