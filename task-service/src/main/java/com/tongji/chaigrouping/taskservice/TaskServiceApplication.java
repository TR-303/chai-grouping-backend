package com.tongji.chaigrouping.taskservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients("com.tongji.chaigrouping.taskservice.client")
@SpringBootApplication
@ComponentScan("com.tongji.chaigrouping")
@MapperScan("com.tongji.chaigrouping.commonutils.mapper")
public class TaskServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }
}
