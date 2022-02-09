package com.kirito.kiritomall.ware.dao;

import com.kirito.kiritomall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author kirito
 * @email 1350221894@qq.com
 * @date 2021-09-29 10:18:07
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(@Param("skuId") Long skuId,@Param("wareId") Long wareId,@Param("skuNum") Integer skuNum);

    Long getHasStock(@Param("sku") Long sku);
}
