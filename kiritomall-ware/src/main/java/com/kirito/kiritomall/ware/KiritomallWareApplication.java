package com.kirito.kiritomall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@MapperScan("com.kirito.kiritomall.ware.dao")
@ComponentScan(basePackages = {"com.kirito"})
@EnableFeignClients
@SpringBootApplication
public class KiritomallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(KiritomallWareApplication.class, args);
    }

}
