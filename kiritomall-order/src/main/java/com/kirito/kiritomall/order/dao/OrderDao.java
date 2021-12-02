package com.kirito.kiritomall.order.dao;

import com.kirito.kiritomall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author kirito
 * @email 1350221894@qq.com
 * @date 2021-09-29 10:15:19
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
