package com.kirito.kiritomall.product.dao;

import com.kirito.kiritomall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author kirito
 * @email 1350221894@qq.com
 * @date 2021-09-28 15:45:13
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
