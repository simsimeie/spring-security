package com.example.authenticationprovider.teacher;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {
    private String id;
    private String username;
    private Set<GrantedAuthority> role;
}
