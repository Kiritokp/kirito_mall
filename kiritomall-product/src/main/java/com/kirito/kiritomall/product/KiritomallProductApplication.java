package com.kirito.kiritomall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@EnableFeignClients(basePackages = "com.kirito.kiritomall.product.feign") //开启远程调用
@EnableDiscoveryClient
@MapperScan("com.kirito.kiritomall.product.dao")
@ComponentScan(basePackages = "com.kirito.kiritomall")
@SpringBootApplication
public class KiritomallProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(KiritomallProductApplication.class, args);
    }
}
