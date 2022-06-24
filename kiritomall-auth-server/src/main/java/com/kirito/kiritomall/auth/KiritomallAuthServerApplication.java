package com.kirito.kiritomall.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = "com.kirito.kiritomall.auth.feign") //开启远程调用
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = "com.kirito.kiritomall")
public class KiritomallAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KiritomallAuthServerApplication.class, args);
    }

}
