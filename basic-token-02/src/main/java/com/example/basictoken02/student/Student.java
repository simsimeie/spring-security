package com.example.basictoken02.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Set<GrantedAuthority> role;
    private String teacherId;
}
