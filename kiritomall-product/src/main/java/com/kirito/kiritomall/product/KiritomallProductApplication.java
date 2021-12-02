package com.kirito.kiritomall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.kirito.kiritomall.product.dao")
@SpringBootApplication
public class KiritomallProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(KiritomallProductApplication.class, args);
    }
}
