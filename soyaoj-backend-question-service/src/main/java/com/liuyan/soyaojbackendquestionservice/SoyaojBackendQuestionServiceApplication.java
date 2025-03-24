package com.liuyan.soyaojbackendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(/*exclude = {RedisAutoConfiguration.class}*/)
@MapperScan("com.liuyan.soyaojbackendquestionservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.liuyan")
public class SoyaojBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoyaojBackendQuestionServiceApplication.class, args);
    }

}
