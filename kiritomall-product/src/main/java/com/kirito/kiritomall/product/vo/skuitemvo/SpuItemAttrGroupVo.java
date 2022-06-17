package com.kirito.kiritomall.product.vo.skuitemvo;

import com.kirito.kiritomall.product.vo.Attr;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author kirito
 * @description: TODO
 * @date 2022-06-17 08:43
 */
@Data
@ToString
public class SpuItemAttrGroupVo {
    private String groupName;
    private List<Attr> attrValues;
}
