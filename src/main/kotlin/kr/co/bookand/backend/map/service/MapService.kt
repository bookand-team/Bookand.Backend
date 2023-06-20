package kr.co.bookand.backend.map.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.exception.BookandException
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
@RequiredArgsConstructor
class MapService {
    @Value("\${kakao.restApiKey}")
    private lateinit var restApiKey: String

    private val BASE_URL = "https://dapi.kakao.com/v2/local/search/keyword.json"

    fun searchByKeyword(query: String, pageable: Pageable): Any? {
        val restTemplate = RestTemplate()
        val objectMapper = ObjectMapper()
        val url = BASE_URL + "?query=" + query + "&page=" + (pageable.pageNumber + 1) + "&size=" + pageable.pageSize
        val headers = HttpHeaders()
        headers.add("Authorization", "KakaoAK $restApiKey")
        val http: HttpEntity<*> = HttpEntity<Any?>(headers)
        val json = try {
            objectMapper.readValue(restTemplate.exchange(url, HttpMethod.GET, http, String::class.java).body, Any::class.java)
        } catch (e: JsonProcessingException) {
            throw BookandException(ErrorCode.JSON_PROCESSING_ERROR)
        }
        return json
    }

}