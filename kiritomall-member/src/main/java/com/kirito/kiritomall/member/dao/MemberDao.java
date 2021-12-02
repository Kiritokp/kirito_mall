package com.kirito.kiritomall.member.dao;

import com.kirito.kiritomall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author kirito
 * @email 1350221894@qq.com
 * @date 2021-09-29 10:04:55
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
