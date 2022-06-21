package com.kirito.kiritomall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author kirito
 * @description: TODO
 * @date 2022-06-17 14:45
 */
@ConfigurationProperties(prefix = "kiritomall.thread")
@Component
@Data
public class ThreadPoolConfigProperties {
    private Integer corePoolSize;
    private Integer maxiMumPoolSize;
    private Integer keepAliveTime;
}
