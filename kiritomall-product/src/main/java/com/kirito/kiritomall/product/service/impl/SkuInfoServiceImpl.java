package com.kirito.kiritomall.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.ParameterMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kirito.common.utils.PageUtils;
import com.kirito.common.utils.Query;

import com.kirito.kiritomall.product.dao.SkuInfoDao;
import com.kirito.kiritomall.product.entity.SkuInfoEntity;
import com.kirito.kiritomall.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils querySkuInfo(Map<String, Object> params) {
        String catelogId = (String) params.get("catelogId");
        String brandId = (String) params.get("brandId");
        String min = (String) params.get("min");
        String max = (String) params.get("max");
        String key = (String) params.get("key");

        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }
        if (!StringUtils.isEmpty(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        if (!StringUtils.isEmpty(key)){
            wrapper.eq("sku_id",key).or().like("sku_name",key);
        }
        wrapper.ge("price",min).le("price",max);

        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkuBySpuId(Long spuId) {
        List<SkuInfoEntity> skuInfoEntities = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return skuInfoEntities;
    }

}