package com.kirito.kiritomall.member.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kirito.common.utils.PageUtils;
import com.kirito.common.utils.Query;

import com.kirito.kiritomall.member.dao.MemberLevelDao;
import com.kirito.kiritomall.member.entity.MemberLevelEntity;
import com.kirito.kiritomall.member.service.MemberLevelService;


@Service("memberLevelService")
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelDao, MemberLevelEntity> implements MemberLevelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<MemberLevelEntity> wrapper = new QueryWrapper<>();
        String keys= (String) params.get("key");
        if (!StringUtils.isEmpty(keys)){
            wrapper.eq("id",keys).or().like("name",keys);
        }
        IPage<MemberLevelEntity> page = this.page(
                new Query<MemberLevelEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}