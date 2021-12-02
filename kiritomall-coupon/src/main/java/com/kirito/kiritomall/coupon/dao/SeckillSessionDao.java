package com.kirito.kiritomall.coupon.dao;

import com.kirito.kiritomall.coupon.entity.SeckillSessionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 秒杀活动场次
 * 
 * @author kirito
 * @email 1350221894@qq.com
 * @date 2021-09-29 09:58:53
 */
@Mapper
public interface SeckillSessionDao extends BaseMapper<SeckillSessionEntity> {
	
}
