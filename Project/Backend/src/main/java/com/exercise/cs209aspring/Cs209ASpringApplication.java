package com.exercise.cs209aspring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.exercise.cs209aspring.mapper")
@SpringBootApplication
public class Cs209ASpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(Cs209ASpringApplication.class, args);
    }

}
