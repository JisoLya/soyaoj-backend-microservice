package com.liuyan.soyaojbackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(/*exclude = {RedisAutoConfiguration.class}*/)
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.liuyan")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.liuyan.soyaojbackendserviceclient.service"})
public class SoyaojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        //初始化消息队列
        InitRabbitMq.doInit();

        SpringApplication.run(SoyaojBackendJudgeServiceApplication.class, args);
    }

}
