package com.example.basictoken02.controller;

import com.example.basictoken02.student.Student;
import com.example.basictoken02.student.StudentAuthenticationProvider;
import com.example.basictoken02.teacher.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/teacher")
public class MobTeacherController {

    @Autowired
    private StudentAuthenticationProvider studentManager;

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/students")
    public List<Student> students(@AuthenticationPrincipal Teacher teacher){
        return studentManager.myStudents(teacher.getId());
    }

}
