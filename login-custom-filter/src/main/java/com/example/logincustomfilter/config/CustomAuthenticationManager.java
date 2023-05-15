package com.example.logincustomfilter.config;

import com.example.logincustomfilter.student.StudentAuthenticationProvider;
import com.example.logincustomfilter.student.StudentAuthenticationToken;
import com.example.logincustomfilter.teacher.TeacherAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {
    private final StudentAuthenticationProvider studentAuthenticationProvider;
    private final TeacherAuthenticationProvider teacherAuthenticationProvider;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(studentAuthenticationProvider.supports(authentication.getClass())){
            return studentAuthenticationProvider.authenticate(authentication);
        } else if(teacherAuthenticationProvider.supports(authentication.getClass())){
            return teacherAuthenticationProvider.authenticate(authentication);
        }

        return null;
    }
}
