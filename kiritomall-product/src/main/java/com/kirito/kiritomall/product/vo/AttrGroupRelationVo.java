package com.kirito.kiritomall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttrGroupRelationVo {
    private Long attrGroupId;//分组id
    private Long attrId;//属性id
}
