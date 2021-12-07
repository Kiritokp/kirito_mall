package com.kirito.kiritomall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttrRespVo extends AttrVo{
    /**
     * 所属属性分组名称
     */
    private String groupName;
    /**
     * 所属分类名称
     */
    private String catelogName;
    /**
     * 所属分类的完整路径
     */
    private Long[] catelogPath;
}
