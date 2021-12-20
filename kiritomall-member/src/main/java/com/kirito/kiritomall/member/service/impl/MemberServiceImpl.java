package com.kirito.kiritomall.member.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kirito.common.utils.PageUtils;
import com.kirito.common.utils.Query;

import com.kirito.kiritomall.member.dao.MemberDao;
import com.kirito.kiritomall.member.entity.MemberEntity;
import com.kirito.kiritomall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<MemberEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.eq("id",key).or().like("username",key);
        }
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}