package com.student.biz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.student.biz.mapper")
public class BizServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BizServiceApplication.class, args);
    }
}
