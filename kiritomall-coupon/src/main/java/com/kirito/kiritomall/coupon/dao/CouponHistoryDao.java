package com.kirito.kiritomall.coupon.dao;

import com.kirito.kiritomall.coupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author kirito
 * @email 1350221894@qq.com
 * @date 2021-09-29 09:58:54
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {
	
}
