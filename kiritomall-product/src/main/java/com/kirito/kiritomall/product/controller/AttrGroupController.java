package com.kirito.kiritomall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.kirito.kiritomall.product.entity.AttrEntity;
import com.kirito.kiritomall.product.service.AttrAttrgroupRelationService;
import com.kirito.kiritomall.product.service.CategoryService;
import com.kirito.kiritomall.product.vo.AttrGroupRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kirito.kiritomall.product.entity.AttrGroupEntity;
import com.kirito.kiritomall.product.service.AttrGroupService;
import com.kirito.common.utils.PageUtils;
import com.kirito.common.utils.R;



/**
 * 属性分组
 *
 * @author kirito
 * @email 1350221894@qq.com
 * @date 2021-09-28 16:31:59
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/list/{catId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@PathVariable("catId") Long catId,@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(catId,params);

        return R.ok().put("page", page);
    }

    /**
     * 根据当前分组获取所属分类列表
     */
    @GetMapping("/list/{catId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R catlogList(@PathVariable("catId") Long catId,@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(catId,params);

        return R.ok().put("page", page);
    }

    /**
     * 获取属性分组关联的所有属性
     */
    @GetMapping("{attrgroupId}/attr/relation")
    public R attrList(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> attrList=attrGroupService.getAttrList(attrgroupId);
        return R.ok().put("attrList",attrList);
    }

    /**
     * 获取属性分组没有关联的其他属性
     */
    @GetMapping("{attrgroupId}/noattr/relation")
    public R noattrList(@PathVariable("attrgroupId") Long attrgroupId,@RequestParam Map<String,Object> params){
        PageUtils page = attrGroupService.getNoAttrList(attrgroupId, params);
        return R.ok().put("page",page);
    }
    /**
     * 新增属性与属性分组的关联关系
     */
    @PostMapping("attr/relation")
    public R saveAttrRelations(@RequestBody List<AttrGroupRelationVo> vos){
        attrGroupService.saveAttrRelations(vos);
        return R.ok();
    }

    @PostMapping("attr/relation/delete")
    public R deleteAttrRelations(@RequestBody List<AttrGroupRelationVo> vos){
        attrGroupService.deleteAttrRelations(vos);
        return R.ok();
    }

    /**
     * 信息
     */
    @GetMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();

        //查询出所属分类的完整路径
        Long[] catelogPath=categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
