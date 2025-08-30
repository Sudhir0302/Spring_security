package com.sudhir003.spring_security.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
public class homecontroller {

    @RequestMapping("/home")
    public String home(HttpServletRequest request){
        return "hello "+request.getSession().getId();
    }

    @RequestMapping("/csrf")
    public Serializable getcsrf(HttpServletRequest request)
    {
        CsrfToken token= (CsrfToken) request.getAttribute("_csrf");
        return token!=null?token:"csrf disabled";
    }

    @RequestMapping("/adduser")
    public String adduser(@RequestParam String username)
    {
        System.out.println(username);

        return "added user : "+username;
    }
}
