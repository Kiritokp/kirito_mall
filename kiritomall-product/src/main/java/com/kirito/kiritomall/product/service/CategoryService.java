package com.kirito.kiritomall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kirito.common.utils.PageUtils;
import com.kirito.kiritomall.product.entity.CategoryEntity;
import com.kirito.kiritomall.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author kirito
 * @email 1350221894@qq.com
 * @date 2021-09-28 15:45:13
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenusByIds(List<Long> asList);

    Long[] findCatelogPath(Long catelogId);

    List<CategoryEntity> getLevel1Categorys();

    Map<String, List<Catelog2Vo>> getCatelogJson();
}

