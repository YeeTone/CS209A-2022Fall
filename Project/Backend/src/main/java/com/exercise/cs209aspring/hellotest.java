package com.exercise.cs209aspring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class hellotest {
    @RequestMapping("/hello")
    public String hello(){
        return "Hello, 11910104";
    }
}
