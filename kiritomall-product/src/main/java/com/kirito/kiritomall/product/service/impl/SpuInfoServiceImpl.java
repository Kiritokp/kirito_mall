package com.kirito.kiritomall.product.service.impl;

import com.kirito.common.to.SkuHasStockVo;
import com.kirito.common.to.SkuReductionTo;
import com.kirito.common.to.SpuBoundsTo;
import com.kirito.common.to.es.SkuEsModel;
import com.kirito.common.utils.R;
import com.kirito.kiritomall.product.entity.*;
import com.kirito.kiritomall.product.feign.CouponFeignService;
import com.kirito.kiritomall.product.feign.WareFeignService;
import com.kirito.kiritomall.product.service.*;
import com.kirito.kiritomall.product.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kirito.common.utils.PageUtils;
import com.kirito.common.utils.Query;

import com.kirito.kiritomall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoService spuInfoService;
    @Autowired
    private SpuImagesService spuImagesService;
    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private ProductAttrValueService productAttrValueService;
    @Autowired
    private CouponFeignService couponFeignService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private WareFeignService wareFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * TODO 高级部分完善
     * @param vo
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {

        //1、保存spu基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        spuInfoService.save(spuInfoEntity);

        //2、保存Spu的描述图片 pms_spu_info_desc
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        List<String> decript = vo.getDecript();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",decript));
        spuInfoDescService.save(spuInfoDescEntity);

        //3、保存spu的图片集 pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(),images);
        //4、保存spu的规格参数;pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            productAttrValueEntity.setAttrId(attr.getAttrId());
            AttrEntity byId = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(byId.getAttrName());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(collect);

        //5、保存spu的积分信息；kiritomall_sms->sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds,spuBoundsTo);
        spuBoundsTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundsTo);
        if (r.getCode()!=0){
            log.error("远程保存spu积分信息失败~");
        }

        //6、保存当前spu对应的所有sku信息；
        List<Skus> skus = vo.getSkus();
        skus.forEach(item->{
            //6.1）、sku的基本信息；pms_sku_info
            String defaultImg="";
            for (Images image : item.getImages()) {
                if (image.getDefaultImg()==1){
                    defaultImg=image.getImgUrl();
                }
            }
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            //    private String skuName;
            //    private BigDecimal price;
            //    private String skuTitle;
            //    private String skuSubtitle;
            BeanUtils.copyProperties(item,skuInfoEntity);
            skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
            skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
            skuInfoEntity.setSaleCount(0L);
            skuInfoEntity.setSpuId(spuInfoEntity.getId());
            skuInfoEntity.setSkuDefaultImg(defaultImg);

            skuInfoService.save(skuInfoEntity);
            //6.2）、sku的图片信息；pms_sku_image
            List<SkuImagesEntity> skuImagesEntityList = item.getImages().stream().map(img -> {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                // private String imgUrl;
                // private int defaultImg;
                BeanUtils.copyProperties(img, skuImagesEntity);
                skuImagesEntity.setSkuId(spuInfoEntity.getId());
                return skuImagesEntity;
            }).filter(entity->{
                //TODO 没有图片路径的无需保存
                //返回true就是需要，返回false就是去除
                return !StringUtils.isEmpty(entity.getImgUrl());
            }).collect(Collectors.toList());
            skuImagesService.saveBatch(skuImagesEntityList);
            //6.3）、sku的销售属性信息：pms_sku_sale_attr_value
            List<Attr> attrs = item.getAttr();
            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attrs.stream().map(attr -> {
                // private Long attrId;
                // private String attrName;
                // private String attrValue;
                SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(attr, skuSaleAttrValueEntity);
                skuSaleAttrValueEntity.setSkuId(spuInfoEntity.getId());
                return skuSaleAttrValueEntity;
            }).collect(Collectors.toList());
            skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

            //6.4）、sku的优惠、满减等信息；kiritomall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price
            SkuReductionTo skuReductionTo = new SkuReductionTo();
            BeanUtils.copyProperties(item,skuReductionTo);
            skuReductionTo.setSkuId(spuInfoEntity.getId());
            if(skuReductionTo.getFullCount() >0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1){
                R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                if (r1.getCode()!=0){
                    log.error("远程保存sku优惠信息失败~");
                }
            }
        });
    }

    @Override
    public PageUtils querySpuInfo(Map<String, Object> params) {
        String catelogId = (String) params.get("catelogId");
        String brandId= (String) params.get("brandId");
        String status = (String) params.get("status");
        String key = (String) params.get("key");

        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }
        if (!StringUtils.isEmpty(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        if (!StringUtils.isEmpty(status)){
            wrapper.eq("publish_status",status);
        }
        if (!StringUtils.isEmpty(key)){
            wrapper.eq("id",key).or().like("spu_name",key);
        }
        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }
    @Override
    public void up(Long spuId) {
        //1、查出当前spuid对应的sku信息，品牌的名字
        List<SkuInfoEntity> skus=skuInfoService.getSkuBySpuId(spuId);
        List<Long> skuIds = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        //TODO 4、查询当前sku可以用来被检索的规格属性
        List<ProductAttrValueEntity> baseAttrs = productAttrValueService.baseAttrlistforspu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        List<Long> searchAttrIds=attrService.selectSearchAttrIds(attrIds);
        HashSet<Long> idSet = new HashSet<>(searchAttrIds);

        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> {
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs);
            return attrs;
        }).collect(Collectors.toList());

        //TODO 1、发送远程调用，库存系统查询是否有库存
        Map<Long, Boolean> stockMap=null;
        try {
            R<List<SkuHasStockVo>> skuHasStock = wareFeignService.getSkuHasStock(skuIds);
            //处理成map(skuId,hasStock)！
            stockMap = skuHasStock.getData().stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));
        }catch (Exception e){
            log.error("库存服务查询异常：原因{}",e);
        }
        //2、封装每个sku信息
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> uoProducts = skus.stream().map(sku -> {
            //组装需要的数据
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, skuEsModel);
            //skuPrice,skuImg
             
            /**
             *     public static class Attrs{
             *         private Long attrId;
             *         private String attrName;
             *         private String attrValue;
             *     }
             */
            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());
            //hasStock,hotScore
            //设置库存信息
            if (finalStockMap ==null){
                skuEsModel.setHasStock(true);
            }else {
                skuEsModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }
            //TODO 2、热度评分，默认0
            skuEsModel.setHotScore(0L);

            //TODO 3、查询品牌和分类的名字
            //brandName,brandImg,catalogName
            BrandEntity brandEntity = brandService.getById(sku.getBrandId());
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());
            CategoryEntity categoryEntity = categoryService.getById(sku.getCatalogId());
            skuEsModel.setCatalogName(categoryEntity.getName());

            //设置检索属性
            skuEsModel.setAttrs(attrsList);

            return skuEsModel;
        }).collect(Collectors.toList());
        //TODO 5、将数据发送给es进行保存
    }
}