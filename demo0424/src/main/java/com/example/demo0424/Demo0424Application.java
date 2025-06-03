package com.example.demo0424;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.demo0424.dao")
public class Demo0424Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo0424Application.class, args);
    }

}
