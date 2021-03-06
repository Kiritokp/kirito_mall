package com.kirito.kiritomall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kirito.common.utils.PageUtils;
import com.kirito.kiritomall.product.entity.CategoryBrandRelationEntity;
import com.kirito.kiritomall.product.vo.BrandVo;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author kirito
 * @email 1350221894@qq.com
 * @date 2021-09-28 15:45:13
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    void updateBrand(Long brandId, String brandName);

    List<BrandVo> getCategoryBrands(Long catId);

}

