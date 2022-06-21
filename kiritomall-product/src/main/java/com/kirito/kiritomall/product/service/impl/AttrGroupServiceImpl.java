package com.kirito.kiritomall.product.service.impl;

import com.kirito.common.constant.ProductConstant;
import com.kirito.kiritomall.product.entity.AttrAttrgroupRelationEntity;
import com.kirito.kiritomall.product.entity.AttrEntity;
import com.kirito.kiritomall.product.service.AttrAttrgroupRelationService;
import com.kirito.kiritomall.product.service.AttrService;
import com.kirito.kiritomall.product.service.CategoryService;
import com.kirito.kiritomall.product.vo.AttrGroupRelationVo;
import com.kirito.kiritomall.product.vo.AttrGroupWithAttrsVo;
import com.kirito.kiritomall.product.vo.skuitemvo.SpuItemAttrGroupVo;
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
        //1.当前属性分组只能关联自己分类里面的所有属性
        AttrGroupEntity attrGroupEntity = this.getById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //2.当前属性分组只能关联到同一分类下其他属性分组没有关联的属性
        //2.1.当前分类下的所有属性分组id
        List<AttrGroupEntity> group = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> attrGroupIds = group.stream().map(item -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());

        //2.2.当前分类下所有分组关联的属性id
        List<AttrAttrgroupRelationEntity> relationEntityList = attrAttrgroupRelationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrGroupIds));
        List<Long> attrIds = relationEntityList.stream().map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        //2.3.从当前分类的所有属性中移除这些属性id
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (attrIds!=null && attrIds.size()>0){
            wrapper.notIn("attr_id",attrIds);
        }
        //3.条件查询
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.eq("attr_id",key).or().like("attr_name",key);
        }
        IPage<AttrEntity> page = attrService.page(new Query<AttrEntity>().getPage(params), wrapper);
        PageUtils pageUtils = new PageUtils(page);

        return pageUtils;
    }

    @Override
    public void saveAttrRelations(List<AttrGroupRelationVo> vos) {
        List<AttrAttrgroupRelationEntity> list = vos.stream().map(attrGroupRelationVo -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
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

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        //1、查出当前分类下的所有属性分组
        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVos = this.list(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId)).stream().map(attrGroupEntity -> {
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(attrGroupEntity, attrGroupWithAttrsVo);
            //2、查出每个属性分组的所有属性
            List<Long> attrIds = attrAttrgroupRelationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_group_id", attrGroupEntity.getAttrGroupId())).stream().map(item -> {
                return item.getAttrId();
            }).collect(Collectors.toList());
            List<AttrEntity> attrEntities = attrService.listByIds(attrIds);
            attrGroupWithAttrsVo.setAttrs(attrEntities);
            return attrGroupWithAttrsVo;
        }).collect(Collectors.toList());

        return attrGroupWithAttrsVos;
    }

    @Override
    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {
        //1.查出当前spu对应的所有属性分组信息以及当前分组下所有属性对应的值
        //1.1当前spu有多少属性分组
        AttrGroupDao baseMapper = this.getBaseMapper();
        List<SpuItemAttrGroupVo> vos = baseMapper.getAttrGroupWithAttrsBySpuId(spuId, catalogId);
        return vos;
    }
}