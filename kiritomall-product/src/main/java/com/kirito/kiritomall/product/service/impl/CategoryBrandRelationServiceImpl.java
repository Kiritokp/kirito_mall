package com.kirito.kiritomall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kirito.kiritomall.product.dao.BrandDao;
import com.kirito.kiritomall.product.entity.BrandEntity;
import com.kirito.kiritomall.product.entity.CategoryEntity;
import com.kirito.kiritomall.product.service.BrandService;
import com.kirito.kiritomall.product.service.CategoryService;
import com.kirito.kiritomall.product.vo.BrandVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kirito.common.utils.PageUtils;
import com.kirito.common.utils.Query;

import com.kirito.kiritomall.product.dao.CategoryBrandRelationDao;
import com.kirito.kiritomall.product.entity.CategoryBrandRelationEntity;
import com.kirito.kiritomall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();

        BrandEntity brand = brandService.getById(brandId);
        CategoryEntity category = categoryService.getById(catelogId);
        categoryBrandRelation.setBrandName(brand.getName());
        categoryBrandRelation.setCatelogName(category.getName());

        boolean save = this.save(categoryBrandRelation);
    }

    @Override
    public void updateBrand(Long brandId, String brandName) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandId(brandId);
        categoryBrandRelationEntity.setBrandName(brandName);

        this.update(categoryBrandRelationEntity,new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId));
    }

    @Override
    public List<BrandVo> getCategoryBrands(Long catId) {
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<CategoryBrandRelationEntity>()
                .eq("catelog_id", catId);
        List<BrandVo> list = this.list(wrapper).stream().map(item -> {
            BrandVo brandVo = new BrandVo();
            BeanUtils.copyProperties(item, brandVo);
            return brandVo;
        }).collect(Collectors.toList());
        return list;
    }

}