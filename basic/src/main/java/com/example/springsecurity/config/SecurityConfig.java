package com.example.springsecurity.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
public class SecurityConfig {
    private final CustomAuthDetail customAuthDetail;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        final PasswordEncoder pw = passwordEncoder();
        UserDetails user2 = User.builder()
                .username("user2")
                .password(pw.encode("1234"))
                .roles("USER").build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(pw.encode("1234"))
                .roles("ADMIN").build();

        //authentication provider를 추가하지 않았기 때문에
        return new InMemoryUserDetailsManager(user2, admin);
    }

    @Bean
    public RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

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
                                .permitAll()
                                // login 성공했을 때 진입할 페이지를 지정
                                // defaultSuccessUrl 메서드를 활용하여 지정하지 않으면 계속 login 페이지로 리다이렉션 된다.
                                .defaultSuccessUrl("/", false)
                                // login 실패시 처리
                                .failureUrl("/login-error")
                                // authenticationDetailsSource 핸들링
                                .authenticationDetailsSource(customAuthDetail)
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
}
