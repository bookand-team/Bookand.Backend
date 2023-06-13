package kr.co.bookand.backend.config.jwt.exception

import kr.co.bookand.backend.common.KotlinErrorCode
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ExceptionHandlerFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (ex: JwtException) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ex.errorCode)
        } catch (ex: RuntimeException) {
            setErrorResponse(HttpStatus.BAD_REQUEST, response, KotlinErrorCode.INTER_SERVER_ERROR)
        }
    }

    private fun setErrorResponse(
        status: HttpStatus,
        response: HttpServletResponse,
        ex: KotlinErrorCode
    ) {
        response.status = status.value()
        response.contentType = "application/json"
        response.characterEncoding = "utf-8"
        val errorResponse = JwtException(ex, ex.errorLog)
        try {
            val json = errorResponse.convertToJson(ex.errorLog)
            response.writer.write(json)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}