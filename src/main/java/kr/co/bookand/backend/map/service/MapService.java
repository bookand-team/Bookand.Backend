package kr.co.bookand.backend.map.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.bookand.backend.common.exception.ErrorCode;
import kr.co.bookand.backend.map.exception.MapException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MapService {
    @Value("${kakao.restApiKey}")
    private String restApiKey;

    private static final String BASE_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";

    public Object searchByKeyword(String query, Pageable pageable) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        String url = BASE_URL + "?query=" + query + "&page=" + (pageable.getPageNumber() + 1) + "&size=" + pageable.getPageSize();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + restApiKey);
        HttpEntity http = new HttpEntity(headers);
        Object json;
        try {
            json = objectMapper.readValue(restTemplate.exchange(url, HttpMethod.GET, http, String.class).getBody(), Object.class);
        } catch (JsonProcessingException e) {
            throw new MapException(ErrorCode.JSON_PROCESSING_ERROR, e.getMessage());
        }
        return json;
    }
}
