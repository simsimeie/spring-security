package com.example.logincustomfilter.config;

import com.example.logincustomfilter.student.StudentAuthenticationToken;
import com.example.logincustomfilter.teacher.TeacherAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
    public CustomLoginFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = obtainUsername(request);
        username = (username != null) ? username : "";
        username = username.strip();
        String password = obtainPassword(request);
        password = (password != null) ? password : "";
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
