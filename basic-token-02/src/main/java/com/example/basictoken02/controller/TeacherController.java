package com.example.basictoken02.controller;

import com.example.basictoken02.student.StudentAuthenticationProvider;
import com.example.basictoken02.teacher.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final StudentAuthenticationProvider studentAuthenticationProvider;

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/main")
    public String main(@AuthenticationPrincipal Teacher teacher, Model model){
        model.addAttribute("studentList", studentAuthenticationProvider.myStudents(teacher.getId()));
        return "TeacherMain";
    }


}
