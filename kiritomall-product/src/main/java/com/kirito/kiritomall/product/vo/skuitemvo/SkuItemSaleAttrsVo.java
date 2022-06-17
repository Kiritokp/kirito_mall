package com.kirito.kiritomall.product.vo.skuitemvo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author kirito
 * @description: TODO
 * @date 2022-06-17 08:42
 */
@Data
@ToString
public class SkuItemSaleAttrsVo {
    private Long attrId;
    private String attrName;
    private List<AttrValueWithSkuIdVo> attrValues;
}
