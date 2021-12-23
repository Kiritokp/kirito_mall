package com.kirito.kiritomall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.kirito.kiritomall.ware.dao")
@EnableFeignClients
@SpringBootApplication
public class KiritomallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(KiritomallWareApplication.class, args);
    }

}
