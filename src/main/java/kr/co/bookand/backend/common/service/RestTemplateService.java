package kr.co.bookand.backend.common.service;

import kr.co.bookand.backend.account.domain.dto.AppleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RestTemplateService<T> {

    private final RestTemplate restTemplate;

    @Autowired
    public RestTemplateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Map<String, Object>> httpEntityPost(String url, HttpMethod method, HttpEntity<T> httpHeaders, ParameterizedTypeReference<Map<String, Object>> response) {
        return restTemplate.exchange(url, method, httpHeaders, response);
    }

    public ResponseEntity<AppleDto> getAppleKeys(String url, HttpMethod method, HttpEntity<MultiValueMap<String, String>> httpHeaders, Class<AppleDto> clazz) {
        return restTemplate.exchange(url, method, httpHeaders, clazz);
    }


}