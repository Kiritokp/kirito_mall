package com.kirito.kiritomall.product.vo.skuitemvo;

import com.kirito.kiritomall.product.entity.SkuImagesEntity;
import com.kirito.kiritomall.product.entity.SkuInfoEntity;
import com.kirito.kiritomall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @author kirito
 * @description: TODO
 * @date 2022-06-15 22:34
 */
@Data
public class SkuItemVo {
    //1.sku基本信息获取 pms_sku_info
    SkuInfoEntity info;

    boolean hasStock = true;
    //2.sku图片信息 pms_sku_images
    List<SkuImagesEntity> imagesEntites;

    //3.spu的销售属性组合
    List<SkuItemSaleAttrsVo> saleAttr;

    //4.spu的详细介绍
    SpuInfoDescEntity desp;

    //5.规格参数
    List<SpuItemAttrGroupVo> groupAttrs;

}
