package com.tongji.chaigrouping.commonutils.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tongji.chaigrouping.commonutils.entity.JoinRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JoinRequestMapper extends BaseMapper<JoinRequest> {

    @Select("select * from `join_request` where group_id = #{groupId}")
    public List<JoinRequest> selectByGroupId(Integer groupId);
}
