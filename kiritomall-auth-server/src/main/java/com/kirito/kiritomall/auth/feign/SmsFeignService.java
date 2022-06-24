package com.kirito.kiritomall.auth.feign;

import com.kirito.common.utils.R;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author kirito
 * @description: TODO
 * @date 2022-06-22 16:36
 */
@FeignClient("kiritomall-third-party")
public interface SmsFeignService {
    @PostMapping("/sms/sendMessage")
    R sendMessage(@RequestParam("code") String code, @RequestParam("phone") String phone);
}
