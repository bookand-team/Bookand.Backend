package kr.co.bookand.backend.common

import kr.co.bookand.backend.account.domain.dto.AppleDto
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class RestTemplateService<T> (
    private val restTemplate: RestTemplate
) {
    fun httpEntityPost(url: String, method: HttpMethod, httpHeaders: HttpEntity<T>, response: ParameterizedTypeReference<Map<String, Any>>): ResponseEntity<Map<String, Any>> {
        return restTemplate.exchange(url, method, httpHeaders, response)
    }

    fun getAppleKeys(url: String, method: HttpMethod, httpHeaders: HttpEntity<MultiValueMap<String, String>>, clazz: Class<AppleDto>): ResponseEntity<AppleDto> {
        return restTemplate.exchange(url, method, httpHeaders, clazz)
    }
}