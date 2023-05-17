package com.example.basictoken01.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate client;
    private RestTemplate restTemplate = new RestTemplate();

    private String greetingUrl(){
        return "http://localhost:"+port+"/greeting";
    }

    @DisplayName("1. 인증 실패")
    @Test
    public void test01(){
        // TestRestTemplate로 수행하면, 권한 체크가 설정되어 있더라도 Exception 발생하지 않음
        String response = client.getForObject(greetingUrl(), String.class);
        // Response : null
        System.out.println("Response : " + response);

        HttpClientErrorException httpClientErrorException = assertThrows(
                HttpClientErrorException.class,
                () -> restTemplate.getForObject(greetingUrl(), String.class)
        );
        assertEquals(401, httpClientErrorException.getRawStatusCode());
    }

    @DisplayName("2. 인증 성공[GET] - RestTemplate 활용")
    @Test
    public void test02(){
        HttpHeaders headers = new HttpHeaders();
        // RestTemplate를 사용하면 HTTP Header에 Authorization에 인증받을 ID와 비밀번호를 명시해야 한다.
        headers.add(HttpHeaders.AUTHORIZATION, "Basic "+ Base64.getEncoder().encodeToString("user1:1111".getBytes()));
        HttpEntity entity = new HttpEntity(null, headers);
        ResponseEntity<String> response = client.exchange(greetingUrl(), HttpMethod.GET, entity, String.class);
        assertEquals("hello", response.getBody());
    }

    @DisplayName("3. 인증 성공[GET] - TestRestTemplate")
    @Test
    void test_3() {
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        String resp = testClient.getForObject(greetingUrl(), String.class);
        assertEquals("hello", resp);
    }

    @DisplayName("4. POST 인증")
    @Test
    void test_4() {
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        ResponseEntity<String> resp = testClient.postForEntity(greetingUrl(), "man", String.class);
        assertEquals("hello man", resp.getBody());
    }


}