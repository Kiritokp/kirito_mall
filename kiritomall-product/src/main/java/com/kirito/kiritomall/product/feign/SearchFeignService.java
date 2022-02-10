package com.kirito.kiritomall.product.feign;

import com.kirito.common.to.es.SkuEsModel;
import com.kirito.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("kiritomall-search")
public interface SearchFeignService {
    @PostMapping("search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
