package com.kirito.kiritomall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class KiritomallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(KiritomallCouponApplication.class, args);
    }

}
