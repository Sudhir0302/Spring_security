package com.sudhir003.spring_security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class homecontroller {

    @RequestMapping("/home")
    public String home(){
        return "hello";
    }
}
