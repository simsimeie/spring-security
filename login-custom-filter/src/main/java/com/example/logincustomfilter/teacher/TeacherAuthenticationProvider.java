package com.example.logincustomfilter.teacher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Component
public class TeacherAuthenticationProvider implements AuthenticationProvider, InitializingBean {
    private HashMap<String, Teacher> teacherDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        if(teacherDB.containsKey(token.getCredentials())){
            Teacher teacher = teacherDB.get(token.getCredentials());
            return TeacherAuthenticationToken.builder()
                    .principal(teacher)
                    .details(teacher.getUsername())
                    .authenticated(true)
                    .authorities(teacher.getRole())
                    .build();
        }
        throw new BadCredentialsException("아이디 또는 비밀번호가 부정확합니다.");
    }

    @Override
    // 어떤 Authentication 구현 객체를 인증하는 AuthenticationProvider 인지 명시
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(TeacherAuthenticationToken.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Teacher("baek", "백선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
        ).forEach(t->
                teacherDB.put(t.getId(), t)
        );
    }
}
