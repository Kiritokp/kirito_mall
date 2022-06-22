package com.kirito.kiritomall.thirdparty.controller;

import com.kirito.common.utils.R;
import com.kirito.kiritomall.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author kirito
 * @description: TODO
 * @date 2022-06-22 16:14
 */
@RestController
@RequestMapping("/sms")
public class SmsController {
    @Autowired
    SmsComponent smsComponent;

    /**
     * 提供给其他服务调用
     * @param code
     * @param phone
     * @return
     */
    @PostMapping()
    public R sendMessage(@RequestParam("code") String code,@RequestParam("phone") String phone){
        HashMap<String, Object> param = new HashMap<>();
        param.put("code",code);
        Boolean isSend = smsComponent.sendMessage(param, phone);
        if (isSend){
            return R.ok("短信发送成功~~~");
        }
        return R.error("短信发送失败~~~");
    }
}
