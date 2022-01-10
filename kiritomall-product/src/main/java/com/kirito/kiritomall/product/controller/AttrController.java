package com.kirito.kiritomall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kirito.kiritomall.product.entity.AttrAttrgroupRelationEntity;
import com.kirito.kiritomall.product.entity.AttrGroupEntity;
import com.kirito.kiritomall.product.entity.ProductAttrValueEntity;
import com.kirito.kiritomall.product.service.*;
import com.kirito.kiritomall.product.vo.AttrRespVo;
import com.kirito.kiritomall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kirito.kiritomall.product.entity.AttrEntity;
import com.kirito.common.utils.PageUtils;
import com.kirito.common.utils.R;



/**
 * 商品属性
 *
 * @author kirito
 * @email 1350221894@qq.com
 * @date 2021-09-28 16:31:59
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {

    @Autowired
    private AttrService attrService;
    @Autowired
    private ProductAttrValueService productAttrValueService;

    /**
     * 获取spu规格
     */
    @GetMapping("/base/listforspu/{spuId}")
    //@RequiresPermissions("product:attr:list")
    public R spuInfo(@PathVariable("spuId")Long spuId){
        List<ProductAttrValueEntity> data=productAttrValueService.getSpuInfo(spuId);

        return R.ok().put("data", data);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }
    /**
     * 条件查询带分页列表 base:基本属性 1  sale:销售属性 0   基本属性和销售属性:2
     */
    @GetMapping("{attrType}/list/{catId}")
    //@RequiresPermissions("product:attr:list")
    public R attrList(@PathVariable("catId") Long catId,
                          @RequestParam Map<String, Object> params,
                          @PathVariable("attrType") String attrType){
        PageUtils page = attrService.queryPageDetail(catId,params,attrType);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVo attrRespVo=attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attrVo){
		attrService.saveDetail(attrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attrVo){
		attrService.updateAttr(attrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update/{spuId}")
    //@RequiresPermissions("product:attr:update")
    public R updateSpuInfo(@PathVariable("spuId")Long spuId,@RequestBody List<ProductAttrValueEntity> productAttrValueEntities){
        productAttrValueService.updateSpuIAttr(spuId,productAttrValueEntities);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
