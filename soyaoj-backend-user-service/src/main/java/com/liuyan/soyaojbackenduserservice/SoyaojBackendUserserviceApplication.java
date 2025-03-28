package com.liuyan.soyaojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(/*exclude = {RedisAutoConfiguration.class}*/)
@MapperScan("com.liuyan.soyaojbackenduserservice.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.liuyan")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.liuyan.soyaojbackendserviceclient.service"})
public class SoyaojBackendUserserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoyaojBackendUserserviceApplication.class, args);
    }

}
