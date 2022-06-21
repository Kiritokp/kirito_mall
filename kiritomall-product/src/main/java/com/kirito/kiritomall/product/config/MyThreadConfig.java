package com.kirito.kiritomall.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author kirito
 * @description: TODO
 * @date 2022-06-17 14:44
 */
@Configuration
public class MyThreadConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties pool){
        return new ThreadPoolExecutor(
            pool.getCorePoolSize(),
            pool.getMaxiMumPoolSize(),
            pool.getKeepAliveTime(),
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(100000),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
