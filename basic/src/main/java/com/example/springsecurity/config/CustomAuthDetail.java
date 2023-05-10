package com.example.springsecurity.config;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
public class CustomAuthDetail implements AuthenticationDetailsSource<HttpServletRequest, RequestInfo> {
    // 예를들어 login시에 request에 담겨온 정보를 Set하기 원하는 경우 사용할 수 있다.
    @Override
    public RequestInfo buildDetails(HttpServletRequest request) {
        return RequestInfo.builder()
                .loginTime(LocalDateTime.now())
                .remoteIp(request.getRemoteAddr())
                .sessionId(request.getSession().getId())
                .build();
    }
}
