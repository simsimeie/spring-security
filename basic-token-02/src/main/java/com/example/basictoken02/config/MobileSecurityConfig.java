package com.example.basictoken02.config;

import com.example.basictoken02.student.StudentAuthenticationProvider;
import com.example.basictoken02.teacher.TeacherAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Order(1)
@Configuration
@RequiredArgsConstructor
public class MobileSecurityConfig {
    private final StudentAuthenticationProvider studentAuthenticationProvider;
    private final TeacherAuthenticationProvider teacherAuthenticationProvider;

    @Bean
    public SecurityFilterChain mobileFilterChain(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .authenticationProvider(studentAuthenticationProvider)
                .authenticationProvider(teacherAuthenticationProvider)
                .csrf().disable()
                .authorizeRequests(request -> request.anyRequest().authenticated())
                .httpBasic();

        return http.build();
    }
}
