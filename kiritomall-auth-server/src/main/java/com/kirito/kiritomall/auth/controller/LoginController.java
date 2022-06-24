package com.kirito.kiritomall.auth.controller;

import com.kirito.common.utils.R;
import com.kirito.kiritomall.auth.feign.SmsFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author kirito
 * @description: TODO
 * @date 2022-06-22 08:50
 */
@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    SmsFeignService smsFeignService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone") String phone){
        //接口防刷
        //验证码二次校验 redis

        String code = UUID.randomUUID().toString().substring(0, 5);
        smsFeignService.sendMessage(code,phone);
        return R.ok();
    }

}
