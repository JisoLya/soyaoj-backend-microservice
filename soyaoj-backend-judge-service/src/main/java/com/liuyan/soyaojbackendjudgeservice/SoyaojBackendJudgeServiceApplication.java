package com.liuyan.soyaojbackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(/*exclude = {RedisAutoConfiguration.class}*/)
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.liuyan")
public class SoyaojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoyaojBackendJudgeServiceApplication.class, args);
    }

}
