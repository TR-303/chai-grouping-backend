package com.tongji.chaigrouping.groupservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients("com.tongji.chaigrouping.groupservice.client")
@MapperScan("com.tongji.chaigrouping.commonutils.mapper")
@SpringBootApplication
@ComponentScan("com.tongji.chaigrouping")
public class GroupServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GroupServiceApplication.class, args);
    }
}
