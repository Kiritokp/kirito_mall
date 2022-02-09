package com.kirito.kiritomall.product.feign;

import com.kirito.common.to.SkuHasStockVo;
import com.kirito.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("kiritomall-ware")
public interface WareFeignService {
    /**
     * 根据skuId查询sku是否有库存
     */
    @PostMapping("ware/waresku/hasstock")
    public R<List<SkuHasStockVo>> getSkuHasStock(@RequestBody List<Long> skuIds);
}
