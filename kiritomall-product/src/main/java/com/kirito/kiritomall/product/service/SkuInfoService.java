package com.kirito.kiritomall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kirito.common.utils.PageUtils;
import com.kirito.kiritomall.product.entity.SkuInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * sku信息
 *
 * @author kirito
 * @email 1350221894@qq.com
 * @date 2021-09-28 15:45:13
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils querySkuInfo(Map<String, Object> params);

    List<SkuInfoEntity> getSkuBySpuId(Long spuId);
}

