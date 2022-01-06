package com.kirito.kiritomall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kirito.common.utils.PageUtils;
import com.kirito.kiritomall.ware.entity.PurchaseEntity;
import com.kirito.kiritomall.ware.vo.MergeVo;

import java.util.Map;

/**
 * 采购信息
 *
 * @author kirito
 * @email 1350221894@qq.com
 * @date 2021-09-29 10:18:07
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryUnReceiveList(Map<String, Object> params);

    void merge(MergeVo mergeVo);
}

