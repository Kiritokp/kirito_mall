package com.kirito.kiritomall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kirito.common.utils.PageUtils;
import com.kirito.kiritomall.product.entity.AttrEntity;
import com.kirito.kiritomall.product.vo.AttrRespVo;
import com.kirito.kiritomall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author kirito
 * @email 1350221894@qq.com
 * @date 2021-09-28 15:45:13
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageDetail(Long catId,Map<String, Object> params,String attrType);

    void saveDetail(AttrVo attrVo);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attrVo);

    /**
     * 在指定的所有属性集合里，挑选出检索属性
     * @param attrIds
     * @return
     */
    List<Long> selectSearchAttrIds(List<Long> attrIds);
}

