package com.example.logincustomfilter.config;


import com.example.logincustomfilter.student.StudentAuthenticationProvider;
import com.example.logincustomfilter.teacher.TeacherAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final StudentAuthenticationProvider studentAuthenticationProvider;
    private final TeacherAuthenticationProvider teacherAuthenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomLoginFilter customLoginFilter) throws Exception {
        http
                .authorizeHttpRequests(request ->
                        request
                                // index 페이지는 모두 접근 가능
                                .antMatchers("/", "/login").permitAll()
                                // 나머지 모든 리퀘스트는 권한 필요
                                .anyRequest().authenticated()
                )
                .formLogin(
                        login -> login.loginPage("/login")
                                .permitAll()
                                .defaultSuccessUrl("/", false)
                                .failureUrl("/login-error")
                )
                // UsernamePasswordAuthenticationFilter 자리에 custom login filter를 넣어라
                .addFilterAt(customLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/")
                )
                .exceptionHandling(error ->
                        error.accessDeniedPage("/access-denied")
                );

        return http.build();
    }

    @Bean
    public CustomLoginFilter customLoginFilter(AuthenticationManager authenticationManager) {
        CustomLoginFilter customLoginFilter = new CustomLoginFilter(authenticationManager);

        return customLoginFilter;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // css, js 등 정적컨텐츠에 대해서는 인증없이 access 가능하도록 처리하기 위해
        return (web) -> web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.authenticationProvider(studentAuthenticationProvider);
//        authenticationManagerBuilder.authenticationProvider(teacherAuthenticationProvider);
//        return authenticationManagerBuilder.build();
//    }

}
