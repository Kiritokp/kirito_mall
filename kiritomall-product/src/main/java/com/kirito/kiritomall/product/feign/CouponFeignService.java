package com.kirito.kiritomall.product.feign;

import com.kirito.common.to.SkuReductionTo;
import com.kirito.common.to.SpuBoundsTo;
import com.kirito.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("kiritomall-coupon")
public interface CouponFeignService {

    @PostMapping("coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);

    @PostMapping("coupon/skufullreduction/save")
    R saveSkuReduction(@RequestBody SkuReductionTo skuLadderTo);

}
