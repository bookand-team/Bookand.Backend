package kr.co.bookand.backend.map.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MapService {
    @Value("${kakao.restApiKey}")
    private String restApiKey;

    private static final String BASE_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";

    public String searchByKeyword(String query, Pageable pageable) {
        String url = BASE_URL + "?query=" + query + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + restApiKey);
        HttpEntity http = new HttpEntity(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, http, String.class);
        return result.getBody();
    }
}
