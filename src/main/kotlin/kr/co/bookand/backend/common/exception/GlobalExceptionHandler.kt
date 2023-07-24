package kr.co.bookand.backend.common.exception

import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.config.jwt.exception.JwtException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException
import java.io.IOException
import java.security.SignatureException
import javax.validation.ConstraintViolationException
import javax.validation.ValidationException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BookandException::class)
    fun handleBookandException(ex: BookandException): ResponseEntity<ApiErrorResponse> {
        log.error("GlobalExceptionHandler BookandException {}", ex.message)
        val response = ApiErrorResponse(ex.errorCode, ex.message ?: "")
        return ResponseEntity.status(ex.errorCode.httpStatus).body(response)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ApiErrorResponse> {
        log.error("GlobalExceptionHandler IllegalArgumentException {}", ex.message)
        val response = ApiErrorResponse(ErrorCode.INPUT_VALID_ERROR, ex.message ?: "")
        return ResponseEntity.status(ErrorCode.INPUT_VALID_ERROR.httpStatus).body(response)
    }

    @ExceptionHandler(JwtException::class)
    fun handleJwtException(ex: JwtException): ResponseEntity<ApiErrorResponse> {
        log.error("GlobalExceptionHandler JwtException {}", ex.message)
        val response = ApiErrorResponse(ErrorCode.JWT_ERROR, ex.message ?: "")
        return ResponseEntity.status(ex.errorCode.httpStatus).body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ApiErrorResponse> {
        log.warn("GlobalExceptionHandler Exception {} - {}", ex::class.simpleName, ex.message)
        val response = ApiErrorResponse(ErrorCode.INTER_SERVER_ERROR, ex.message ?: "")
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.httpStatus).body(response)
    }

    @ExceptionHandler(IOException::class)
    fun handleIOException(ex: IOException): ResponseEntity<ApiErrorResponse> {
        log.error("GlobalExceptionHandler IOException {}", ex.message)
        val response = ApiErrorResponse(ErrorCode.INTER_SERVER_ERROR, ex.message ?: "")
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.httpStatus).body(response)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(ex: HttpRequestMethodNotSupportedException): ResponseEntity<ApiErrorResponse> {
        log.error("GlobalExceptionHandler HttpRequestMethodNotSupportedException {}", ex.message)
        val response = ApiErrorResponse(ErrorCode.INTER_SERVER_ERROR, ex.message ?: "")
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.httpStatus).body(response)
    }

    @ExceptionHandler(HttpClientErrorException::class)
    fun handleHttpClientErrorException(ex: HttpClientErrorException): ResponseEntity<ApiErrorResponse> {
        log.error("GlobalExceptionHandler HttpClientErrorException {}", ex.message)
        val response = ApiErrorResponse(ErrorCode.HTTP_CLIENT_ERROR, ex.message ?: "")
        return ResponseEntity.status(ErrorCode.HTTP_CLIENT_ERROR.httpStatus).body(response)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(ex: ValidationException): ResponseEntity<ApiErrorResponse> {
        log.error("GlobalExceptionHandler ValidationException {}", ex.message)
        val response = ApiErrorResponse(ErrorCode.INPUT_VALID_ERROR, ex.message ?: "")
        return ResponseEntity.status(ErrorCode.INPUT_VALID_ERROR.httpStatus).body(response)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ApiErrorResponse> {
        log.warn("GlobalExceptionHandler ConstraintViolationException {} - {}", ex::class.simpleName, ex.message)
        val message = ex.constraintViolations.iterator().next().message
        val response = ApiErrorResponse(ErrorCode.INPUT_VALID_ERROR, message)
        return ResponseEntity.status(ErrorCode.INPUT_VALID_ERROR.httpStatus).body(response)
    }


    @ExceptionHandler(SignatureException::class)
    fun handleSignatureException(ex: SignatureException): ResponseEntity<ApiErrorResponse> {
        log.error("GlobalExceptionHandler SignatureException {}", ex.message)
        val response = ApiErrorResponse(ErrorCode.JWT_ERROR_SIGNATURE, ex.message ?: "")
        return ResponseEntity.status(ErrorCode.JWT_ERROR_SIGNATURE.httpStatus).body(response)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ApiErrorResponse> {
        log.error("GlobalExceptionHandler RuntimeException {}", ex.message)
        val response = ApiErrorResponse(ErrorCode.INTER_SERVER_ERROR, ex.message ?: "")
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.httpStatus).body(response)
    }

    @ExceptionHandler(NullPointerException::class)
    fun handleNullPointerException(ex: NullPointerException): ResponseEntity<ApiErrorResponse> {
        log.error("GlobalExceptionHandler NullPointerException {}", ex.message)
        val response = ApiErrorResponse(ErrorCode.INTER_SERVER_ERROR, ex.message ?: "")
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.httpStatus).body(response)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(ex: NoSuchElementException): ResponseEntity<ApiErrorResponse> {
        log.error("GlobalExceptionHandlerNoSuchElementException {}", ex.message)
        val response = ApiErrorResponse(ErrorCode.INTER_SERVER_ERROR, ex.message ?: "")
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.httpStatus).body(response)
    }

    @ExceptionHandler(UnsupportedOperationException::class)
    fun handleUnsupportedOperationException(ex: UnsupportedOperationException): ResponseEntity<ApiErrorResponse> {
        log.error("GlobalExceptionHandler UnsupportedOperationException {}", ex.message)
        val response = ApiErrorResponse(ErrorCode.INTER_SERVER_ERROR, ex.message ?: "")
        return ResponseEntity.status(ErrorCode.INTER_SERVER_ERROR.httpStatus).body(response)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException): ResponseEntity<ApiErrorResponse> {
        log.error("GlobalExceptionHandler BadCredentialsException {}", ex.message)
        val response = ApiErrorResponse(ErrorCode.JWT_ERROR, ex.message ?: "")
        return ResponseEntity.status(ErrorCode.JWT_ERROR.httpStatus).body(response)
    }

}