package com.kirito.kiritomall.ware.feign;

import com.kirito.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("kiritomall-product")
//@FeignClient("kiritomall-gateway")
public interface ProductFeignService {
    /**
     * 远程调用的两种方式
     * 1./product/skuinfo/info/{skuId} 直接调用对用的服务
     * 2./api/product/skuinfo/info/{skuId} 所有请求都要通过网关
     * @param skuId
     * @return
     */
    @GetMapping("product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
