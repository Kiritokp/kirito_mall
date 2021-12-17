package com.kirito.kiritomall.product.service.impl;

import com.kirito.kiritomall.product.entity.AttrAttrgroupRelationEntity;
import com.kirito.kiritomall.product.entity.AttrEntity;
import com.kirito.kiritomall.product.service.AttrAttrgroupRelationService;
import com.kirito.kiritomall.product.service.AttrService;
import com.kirito.kiritomall.product.service.CategoryService;
import com.kirito.kiritomall.product.vo.AttrGroupRelationVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kirito.common.utils.PageUtils;
import com.kirito.common.utils.Query;

import com.kirito.kiritomall.product.dao.AttrGroupDao;
import com.kirito.kiritomall.product.entity.AttrGroupEntity;
import com.kirito.kiritomall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Long catId,Map<String, Object> params) {
        //获取key
        String key= (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        // select * from pms_attr_group where catelog_id =? and (attr_group_id =key or attr_group_name =key);
        if (!StringUtils.isEmpty(key)){
            wrapper.eq("attr_group_id",key).or().like("attr_group_name",key);
        }
        if (catId!=0){
            wrapper.eq("catelog_id",catId);
        }

        IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    public List<AttrEntity> getAttrList(Long attrgroupId) {
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_group_id",attrgroupId);
        List<AttrEntity> list = attrAttrgroupRelationService.list(wrapper).stream().map(attrAttrgroupRelationEntity -> {
            Long attrId = attrAttrgroupRelationEntity.getAttrId();
            AttrEntity attrEntity = attrService.getById(attrId);
            return attrEntity;
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public PageUtils getNoAttrList(Long attrgroupId,Map<String,Object> params) {
        //需要显示的属性的特征
        //1、当前分类下这个属性分组没有关联的
        //1.1、根据属性分组id得到当前属性分组下已经关联的属性id
        List<Long> attrIdList = attrAttrgroupRelationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_group_id", attrgroupId))
                .stream().map(attrAttrgroupRelationEntity -> {
                    Long attrId = attrAttrgroupRelationEntity.getAttrId();
                    return attrId;
                }).collect(Collectors.toList());

        //2、当前分类下其他属性分组没有关联的
        AttrGroupEntity attrGroupEntity = this.getById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();

        QueryWrapper<AttrGroupEntity> groupEntityQueryWrapper = new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId)
                .ne("attr_group_id", attrgroupId);
        List<Long> attrGroupIdList = baseMapper.selectList(groupEntityQueryWrapper).stream()
                .map(attrGroupEntity1 -> {
                    Long attrGroupId1 = attrGroupEntity1.getAttrGroupId();
                    return attrGroupId1;
                }).collect(Collectors.toList());
        List<Long> attrIdList2 = attrAttrgroupRelationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .in("attr_group_id", attrGroupIdList))
                .stream().map(attrAttrgroupRelationEntity -> {
                    Long attrId1 = attrAttrgroupRelationEntity.getAttrId();
                    return attrId1;
                }).collect(Collectors.toList());

        QueryWrapper<AttrEntity> attrQueryWrapper = new QueryWrapper<AttrEntity>();
        if(attrIdList!=null &&attrIdList.size()!=0){
            attrQueryWrapper.notIn("attr_id", attrIdList);
        }
        if(attrIdList2!=null &&attrIdList2.size()!=0){
            attrQueryWrapper.notIn("attr_id", attrIdList2);
        }
        attrQueryWrapper.eq("attr_type",1);

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            attrQueryWrapper.eq("attr_id",key).or().like("attr_name",key);
        }
        IPage<AttrEntity> page = attrService.page(new Query<AttrEntity>().getPage(params), attrQueryWrapper);
        PageUtils pageUtils = new PageUtils(page);

        return pageUtils;
    }

    @Override
    public void saveAttrRelations(List<AttrGroupRelationVo> vos) {
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        List<AttrAttrgroupRelationEntity> list = vos.stream().map(attrGroupRelationVo -> {
            BeanUtils.copyProperties(attrGroupRelationVo, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        attrAttrgroupRelationService.saveBatch(list);
    }

    @Override
    public void deleteAttrRelations(List<AttrGroupRelationVo> vos) {
        for (AttrGroupRelationVo attrGroupRelationVo : vos) {
            Long attrId = attrGroupRelationVo.getAttrId();
            Long attrGroupId = attrGroupRelationVo.getAttrGroupId();
            QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("attr_id",attrId).eq("attr_group_id",attrGroupId);

            attrAttrgroupRelationService.remove(wrapper);
        }
    }

}