package com.example.authenticationprovider.config;

import com.example.authenticationprovider.student.StudentAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig implements WebSecurityConfigurer {
    private final StudentAuthenticationProvider studentAuthenticationProvider;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request ->
                        request
                                // index 페이지는 모두 접근 가능
                                .antMatchers("/").permitAll()
                                // 나머지 모든 리퀘스트는 권한 필요
                                .anyRequest().authenticated()
                )
                .formLogin(login ->
                        // Default login page가 아닌 특정 페이지를 지정하려면 아래와 같이 설정
                        login
                                .loginPage("/login")
                                // permitAll 메서드를 활용하지 않으면 계속 login 페이지로 리다이렉션 된다.
                                .permitAll()
                                // login 성공했을 때 진입할 페이지를 지정
                                .defaultSuccessUrl("/", false)
                                // login 실패시 처리
                                .failureUrl("/login-error")
                                // authenticationDetailsSource 핸들링
                )
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
    public WebSecurityCustomizer webSecurityCustomizer() {
        // css, js 등 정적컨텐츠에 대해서는 인증없이 access 가능하도록 처리하기 위해
        return (web) -> web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }


    @Override
    public void init(SecurityBuilder builder) throws Exception {

    }

    @Override
    public void configure(SecurityBuilder builder) throws Exception {
        AuthenticationManagerBuilder auth = (AuthenticationManagerBuilder) builder;
        auth.authenticationProvider(studentAuthenticationProvider);
    }
}
