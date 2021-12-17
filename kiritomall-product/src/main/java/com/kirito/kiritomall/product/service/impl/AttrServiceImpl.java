package com.kirito.kiritomall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kirito.common.constant.ProductConstant;
import com.kirito.kiritomall.product.entity.AttrAttrgroupRelationEntity;
import com.kirito.kiritomall.product.entity.AttrGroupEntity;
import com.kirito.kiritomall.product.entity.CategoryEntity;
import com.kirito.kiritomall.product.service.AttrAttrgroupRelationService;
import com.kirito.kiritomall.product.service.AttrGroupService;
import com.kirito.kiritomall.product.service.CategoryService;
import com.kirito.kiritomall.product.vo.AttrRespVo;
import com.kirito.kiritomall.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
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

import com.kirito.kiritomall.product.dao.AttrDao;
import com.kirito.kiritomall.product.entity.AttrEntity;
import com.kirito.kiritomall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import sun.nio.cs.ArrayEncoder;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageDetail(Long catId,Map<String, Object> params,String attrType) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("attr_type","base".equalsIgnoreCase(attrType)
                        ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()
                        :ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        String key = (String) params.get("key");

        if (!StringUtils.isEmpty(key)){
            wrapper.eq("attr_id",key).or().like("attr_name",key);
        }
        if (catId!=0){
            wrapper.eq("catelog_id",catId);
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        PageUtils pageUtils = new PageUtils(page);

        //获取分页所有记录
        List<AttrEntity> records = page.getRecords();

        List<AttrRespVo> respVos = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //如果是销售属性就不用设置分组信息
            if("base".equalsIgnoreCase(attrType)){
                //设置分类和属性分组的名字
                //获取分组id
                AttrAttrgroupRelationEntity attrAttrgroupRelationServiceOne = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", attrEntity.getAttrId()));
                if(attrAttrgroupRelationServiceOne!=null){
                    Long groupId = attrAttrgroupRelationServiceOne.getAttrGroupId();
                    AttrGroupEntity attrGroupEntity = attrGroupService.getById(groupId);
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            Long catelogId = attrEntity.getCatelogId();
            CategoryEntity categoryEntity = categoryService.getById(catelogId);
            attrRespVo.setCatelogName(categoryEntity.getName());
            return attrRespVo;
        }).collect(Collectors.toList());

        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Transactional
    @Override
    public void saveDetail(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo,attrEntity);
        //保存基本信息
        this.save(attrEntity);
        if (attrVo.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //保存关联关系
            //新增属性信息的时候需要保存信息到pms_attr_attrgroup_relation表中
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
        }
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrRespVo attr = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity,attr);
        //1、设置分类信息
        Long[] catelogPath = categoryService.findCatelogPath(attrEntity.getCatelogId());
        attr.setCatelogPath(catelogPath);
        CategoryEntity categoryEntity = categoryService.getById(attrEntity.getCatelogId());
        if (categoryEntity!=null) {
            attr.setCatelogName(categoryEntity.getName());
        }
        if (attr.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //2、设置分组信息
            AttrAttrgroupRelationEntity attrgroupRelationServiceOne = attrAttrgroupRelationService.getOne(
                    new QueryWrapper<AttrAttrgroupRelationEntity>()
                            .eq("attr_id", attrEntity.getAttrId()));
            if (attrgroupRelationServiceOne!=null){
                Long attrGroupId = attrgroupRelationServiceOne.getAttrGroupId();
                attr.setAttrGroupId(attrGroupId);
                AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
                if (attrGroup!=null){
                    attr.setGroupName(attrGroup.getAttrGroupName());
                }
            }
        }
        return attr;
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo,attrEntity);
        //1、修改基本信息
        this.updateById(attrEntity);
        if(attrVo.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //2、修改分组关联
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrVo.getAttrId());
            int count = attrAttrgroupRelationService.count(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrVo.getAttrId()));
            if (count>0){
                //修改
                attrAttrgroupRelationService.update(attrAttrgroupRelationEntity,
                        new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attrVo.getAttrId()));
            }else {
                //新增
                attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
            }
        }
    }

}