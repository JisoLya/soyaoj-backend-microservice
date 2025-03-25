package com.liuyan.soyaojcommon.soyaojbackendgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SoyaojBackendGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoyaojBackendGatewayApplication.class, args);
    }

}
