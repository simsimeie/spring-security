package com.example.logincustomfilter.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }

    @GetMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("loginError", true);
        return "loginForm";
    }
    @GetMapping("/access-denied")
    public String accessDenied(){
        return "AccessDenied";
    }

    @RequestMapping("/auth")
    @ResponseBody
    public Authentication auth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }


}
