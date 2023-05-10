package com.security.authenticationprovider.teacher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
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
        if(teacherDB.containsKey(token.getName())){
            Teacher teacher = teacherDB.get(token.getName());
            return TeacherAuthenticationToken.builder()
                    .principal(teacher)
                    .details(teacher.getUsername())
                    .authenticated(true)
                    .authorities(teacher.getRole())
                    .build();
        }
        return null;
    }

    @Override
    // 어떤 Authentication 구현 객체를 인증하는 AuthenticationProvider 인지 명시
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
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
