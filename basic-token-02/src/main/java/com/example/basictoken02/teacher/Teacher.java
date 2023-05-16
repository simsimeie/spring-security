package com.example.basictoken02.teacher;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Set<GrantedAuthority> role;
}
