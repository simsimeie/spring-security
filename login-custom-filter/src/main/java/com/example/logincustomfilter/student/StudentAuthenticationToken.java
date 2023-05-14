package com.example.logincustomfilter.student;

import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAuthenticationToken implements Authentication {
    private Student principal;
    private String credentials;
    private String details;
    private boolean authenticated;
    private Set<GrantedAuthority> authorities;

    @Override
    public String getName() {
        return principal == null ? "" : principal.getId();
    }
}
