package com.kirito.kiritomall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kirito.common.utils.PageUtils;
import com.kirito.kiritomall.product.entity.AttrEntity;
import com.kirito.kiritomall.product.entity.AttrGroupEntity;
import com.kirito.kiritomall.product.vo.AttrGroupRelationVo;
import com.kirito.kiritomall.product.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author kirito
 * @email 1350221894@qq.com
 * @date 2021-09-28 15:45:13
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Long catId,Map<String, Object> params);

    List<AttrEntity> getAttrList(Long attrgroupId);

    PageUtils getNoAttrList(Long attrgroupId,Map<String,Object> params);

    void saveAttrRelations(List<AttrGroupRelationVo> vos);

    void deleteAttrRelations(List<AttrGroupRelationVo> vos);

    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);
}

