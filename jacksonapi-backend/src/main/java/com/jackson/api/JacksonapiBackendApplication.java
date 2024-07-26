package com.jackson.api;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jackson.api.mapper")
@EnableDubbo
public class JacksonapiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(JacksonapiBackendApplication.class, args);
    }

}
