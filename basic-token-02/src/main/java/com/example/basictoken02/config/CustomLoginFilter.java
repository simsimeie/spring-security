package com.example.basictoken02.config;

import com.example.basictoken02.student.StudentAuthenticationToken;
import com.example.basictoken02.teacher.TeacherAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    public CustomLoginFilter(AuthenticationManager authenticationManager, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        super(authenticationManager);
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = obtainUsername(request);
        username = (username != null) ? username : "";
        username = username.strip();
        String password = obtainPassword(request);
        password = (password != null) ? password : "";

        this.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        String type = request.getParameter("type");
        if (type == null || !type.equals("teacher")) {
            StudentAuthenticationToken token = StudentAuthenticationToken.builder().credentials(username).build();
            return this.getAuthenticationManager().authenticate(token);
        } else {
            TeacherAuthenticationToken token = TeacherAuthenticationToken.builder().credentials(username).build();
            return this.getAuthenticationManager().authenticate(token);
        }
    }
}
