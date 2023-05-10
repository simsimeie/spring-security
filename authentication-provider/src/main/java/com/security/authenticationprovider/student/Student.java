package com.security.authenticationprovider.student;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    private String id;
    private String username;
    private Set<GrantedAuthority> role;
}
