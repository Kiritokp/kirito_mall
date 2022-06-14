package com.kirito.kiritomall.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.kirito.kiritomall.search.dao")
@ComponentScan(basePackages = "com.kirito.kiritomall")
@EnableFeignClients(basePackages = "com.kirito.kiritomall.product.feign") //开启远程调用
@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class KiritomallSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(KiritomallSearchApplication.class, args);
    }

}
