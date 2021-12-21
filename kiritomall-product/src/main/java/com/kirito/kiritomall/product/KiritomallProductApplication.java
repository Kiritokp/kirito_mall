package com.kirito.kiritomall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@EnableFeignClients(basePackages = "com.kirito.kiritomall.product.feign")
@MapperScan("com.kirito.kiritomall.product.dao")
@SpringBootApplication
public class KiritomallProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(KiritomallProductApplication.class, args);
    }
}
