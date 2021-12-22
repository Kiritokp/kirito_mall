package com.kirito.kiritomall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.kirito.kiritomall.product.entity.AttrEntity;
import com.kirito.kiritomall.product.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

@Data
public class AttrGroupWithAttrsVo {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;
    /**
     * 当前分组关联的所有属性
     */
    private List<AttrEntity> attrs;
}
