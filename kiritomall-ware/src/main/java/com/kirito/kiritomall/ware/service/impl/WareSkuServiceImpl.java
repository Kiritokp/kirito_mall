package com.kirito.kiritomall.ware.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kirito.common.utils.PageUtils;
import com.kirito.common.utils.Query;

import com.kirito.kiritomall.ware.dao.WareSkuDao;
import com.kirito.kiritomall.ware.entity.WareSkuEntity;
import com.kirito.kiritomall.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String wareId= (String) params.get("wareId");
        String skuId= (String) params.get("skuId");
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(wareId)){
            wrapper.eq("ware_id",wareId);
        }
        if (!StringUtils.isEmpty(skuId)){
            wrapper.eq("sku_id",skuId);
        }
        IPage<WareSkuEntity> page = this.page(new Query<WareSkuEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

}