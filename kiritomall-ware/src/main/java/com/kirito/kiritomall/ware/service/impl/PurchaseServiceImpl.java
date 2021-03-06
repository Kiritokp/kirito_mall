package com.kirito.kiritomall.ware.service.impl;

import com.kirito.common.constant.WareConstant;
import com.kirito.kiritomall.ware.entity.PurchaseDetailEntity;
import com.kirito.kiritomall.ware.service.PurchaseDetailService;
import com.kirito.kiritomall.ware.service.WareSkuService;
import com.kirito.kiritomall.ware.vo.MergeVo;
import com.kirito.kiritomall.ware.vo.PurchaseDetailDoneVo;
import com.kirito.kiritomall.ware.vo.PurchaseDoneVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kirito.common.utils.PageUtils;
import com.kirito.common.utils.Query;

import com.kirito.kiritomall.ware.dao.PurchaseDao;
import com.kirito.kiritomall.ware.entity.PurchaseEntity;
import com.kirito.kiritomall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService purchaseDetailService;
    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String status = (String) params.get("status");
        String key = (String) params.get("key");
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }
        if (!StringUtils.isEmpty(key)){
            wrapper.eq("id",key).or().like("assignee_name",key);
        }
        IPage<PurchaseEntity> page = this.page(new Query<PurchaseEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryUnReceiveList(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();

        wrapper.eq("status", WareConstant.PurchaseStatusEnum.CREATED.getCode())
                .or().eq("status",WareConstant.PurchaseStatusEnum.ASSIGNED.getCode());

        IPage<PurchaseEntity> page = this.page(new Query<PurchaseEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Override
    public void merge(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        //?????????????????????????????????????????????????????????
        if (purchaseId==null){
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId=purchaseEntity.getId();
        }
        //TODO ???????????????????????????0??????1???????????????
        //??????????????????????????????id??????????????????->????????????
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(i);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);

        //?????????????????????????????????????????????????????????
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        this.updateById(purchaseEntity);
    }

    @Override
    public void received(List<Long> ids) {
        //???????????????
        //TODO ????????????????????????????????????????????????
        //?????????????????????????????????
        List<PurchaseEntity> purchaseEntities = this.listByIds(ids);
        List<PurchaseEntity> collect = purchaseEntities.stream().map(purchaseEntity -> {
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            return purchaseEntity;
        }).collect(Collectors.toList());
        this.updateBatchById(collect);
        //??????????????????????????????????????????????????????????????????
        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.list(new QueryWrapper<PurchaseDetailEntity>().in("purchase_id", ids));
        List<PurchaseDetailEntity> list = purchaseDetailEntities.stream().map(purchaseDetailEntity -> {
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(list);
    }

    @Transactional
    @Override
    public void finish(PurchaseDoneVo purchaseDoneVo) {
        //????????????
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        List<PurchaseDetailDoneVo> items = purchaseDoneVo.getItems();
        AtomicBoolean flag= new AtomicBoolean(true);
        List<PurchaseDetailEntity> collect = items.stream().map(purchaseDetailDoneVo -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(purchaseDetailDoneVo.getItemId());
            if (purchaseDetailDoneVo.getStatus()==WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()){
                flag.set(false);
                purchaseDetailEntity.setStatus(purchaseDetailDoneVo.getStatus());
            }else {
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                //??????????????????????????????
                PurchaseDetailEntity entity = purchaseDetailService.getById(purchaseDetailDoneVo.getItemId());
                wareSkuService.addStock(entity.getSkuId(),entity.getWareId(),entity.getSkuNum());
            }
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);
        //?????????????????????????????????????????????????????????????????????????????????,??????????????????????????????????????????
        Long purchaseId = purchaseDoneVo.getId();
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        if (flag.get()){
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.FINISH.getCode());
        }
        purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        this.updateById(purchaseEntity);
    }
}